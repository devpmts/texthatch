package com.dropaline.plugins.apis.toodleDo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringBufferInputStream;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.loststone.toodledo.ToodledoApi;
import org.loststone.toodledo.ToodledoApiImpl;
import org.loststone.toodledo.data.Context;
import org.loststone.toodledo.data.Folder;
import org.loststone.toodledo.data.Priority;
import org.loststone.toodledo.data.Status;
import org.loststone.toodledo.data.Todo;
import org.loststone.toodledo.exception.ToodledoApiException;
import org.loststone.toodledo.util.AuthToken;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.parser.THException;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.e4.DI;

@Singleton
@Creatable
public class ToodledoPimApi extends PimApi<ToodledoAuthenticator, ToodledoPimItem> {

	enum ToodledoErrors {
		FOLDER_NOT_FOUND, ERROR_REQUESTING_FOLDERS, ERROR_REQUESTING_CONTEXTS, ERROR_ADDING_TASK
	};

	public boolean includeCompletedTasks;

	private boolean validationEnabled = true;

	private boolean quickAdd;

	private TreeSet<String> tagExcludeSet = new TreeSet<>();

	private TreeSet<String> tagIncludeSet = new TreeSet<>();

	private static ToodledoApi api = new ToodledoApiImpl();

	private long contextLeaseTimeStamp;

	private List<Context> contexts;

	private List<Folder> folders;

	private ArrayList<Todo> getTasks;

	private int leaseMinutes;

	private TreeSet<String> tagSet = new TreeSet<>();

	@Inject
	@org.eclipse.e4.core.di.annotations.Optional
	@Named(ToodledoAuthenticator.TOODLEDO_AUTHENTICATION_TOKEN)
	AuthToken auth;

	@Inject
	@org.eclipse.e4.core.di.annotations.Optional
	private UserInterface userInterface;

	private String tagText;

	private int taskCount;

	@Inject
	public ToodledoPimApi() {
		super(new ToodledoAuthenticator(api));
		System.err.println("created Toodledo Pim Api");
		quickAdd = DI.get("ToodleDo_quickAdd", false);
		DI.injectContextIn(this);
	}

	public void changeTag(HashMap<String, String> mapping, ArrayList<Todo> tasks) throws ToodledoApiException {
		for (Entry<String, String> entry : mapping.entrySet()) {
			for (Todo task : tasks) {
				if (task.getTag() != null) {
					// String tagString = getCharCodedStr(task.getTag());
					Set<String> tags = convertToodledoTaskToPimItem(task).getTags();
					String found = null;
					for (String tag : tags) {
						if (entry.getKey().trim().equals(tag.trim())) {
							found = tag;
							break;
						}
					}
					if (found != null) {
						tags.remove(found);
						tags.add(entry.getValue().trim());
						String tagString = cleanCommas(tags.toString().substring(1, tags.toString().length() - 1));
						task.setTag(tagString.trim());
						api.modifyTodo(auth, task);
						DevsLogger.log("changed " + entry.getKey() + " to " + task.getTag());
					}
				}
			}
		}
	}

	private String cleanCommas(String s) {
		if (s.contains(",,") || s.contains(", ,")) {
			s = (s.replace(",,", ","));
			s = (s.replace(", ,", ","));
		} else if (s.startsWith(",")) {
			s = s.substring(1);
		} else if (s.endsWith(",")) {
			s = s.substring(0, s.length() - 1);
		}
		return s.trim();
	}

	public void cleanTags() throws ToodledoApiException {
		List<Todo> tasks = (getTasks != null) ? getTasks : getTasks();
		for (Todo task : tasks) {
			if (task.getTag() != null) {
				String s = task.getTag();
				s = cleanCommas(s);
				boolean modified = !s.equals(task.getTag());
				task.setTag(s);
				String[] tagArray = getCharCodedStr(task.getTag()).split(",");
				for (int i = 0; i < tagArray.length; i++) {
					tagArray[i] = tagArray[i].trim();
				}
				Set<String> tags = new TreeSet<>();
				tags.addAll(Arrays.asList(tagArray));
				if (tags.size() < tagArray.length || modified) {
					String tagStr = tags.toString().substring(1, tags.toString().length() - 1);
					task.setTag(tagStr.trim());
					api.modifyTodo(auth, task);
					DevsLogger.log("changed " + Arrays.toString(tagArray) + " to " + tags.toString());
				}
			}
		}
	}

	@Override
	public void deliverPimItem(final ToodledoPimItem item) throws THException {
		Todo task = prepareTask(item);
		String statusString = item.getStatus();
		if (statusString.length() > 0) {
			applyStatus(item, task, statusString);
			if (validationEnabled && task.getStatus() == null) {
				userInterface.handleNotFoundInList(item.getStatus(), Status.values(), "Status");
				return;
			}
		}

		String contextString = item.getContext();
		DevsLogger.log("context is " + contextString);
		if (contextString.length() > 0) {
			if (shouldRequestNewContextList()) {
				requestNewContextList();
			}
			applyContext(item, task, contextString);
			if (validationEnabled && task.getContext() == -1L) {
				List<String> strings = contexts.stream().map(c -> c.getName()).collect(Collectors.toList());
				userInterface.handleNotFoundInList(item.getContext(), strings.toArray(), "Context");
				return;
			}
		}

		String projectString = item.getProject();
		if (projectString.length() > 0) {
			Optional<Folder> folder = getFolder(projectString);
			if (folder.isPresent()) {
				task.setFolder(folder.get().getId());
				item.setProject(folder.get().getSName());
			} else if (validationEnabled) {
				userInterface.handleNotFoundInList(item.getProject(),
						folders.stream().map(f -> f.getSName()).toArray(), "Folder");
				return;
			}

		}

		if (quickAdd) {
			String dueDateString = item.getDueDate();
			if (dueDateString.length() != 0) {
				assert false;
				// task.setEnd(java.time.Clock.dueDateString);
			}
		} else if (item.getStartTime() != 0) {
			// task.setDueDate(item.getStartTime().getTime());
			task.setEnd(item.getStartTime());
			DevsLogger.log("sending to ToodleDo with Date " + item.getStartTime());
		}
		if ((boolean) DI.get(CORE.LEARNING_MODE)) {
			return;
		}

		addTask(task);
	}

	private void addTask(Todo task) throws THException {
		int added;
		try {
			added = api.addTodo(auth, task);
		} catch (ToodledoApiException e) {
			handleApiException(ToodledoErrors.ERROR_ADDING_TASK, e);
			added = -1;
		}
		if (added == -1) {
			throw new THException("toodledo task not sent. Error code " + added);
		}
		DevsLogger.log("###      successfully delivered: " + task.getTitle() + "    ###");
	}

	private void applyContext(final ToodledoPimItem item, Todo task, String contextString) {
		contexts.stream()//
				.filter(c -> c.getName().toLowerCase().startsWith(contextString.toLowerCase()))//
				.forEach(c -> {
					if (task.getContext() != -1L)
						return;
					DevsLogger.log("found target " + c.getName());
					task.setContext(c.getId());
					item.setContextId(c.getId());
				});
	}

	private void requestNewContextList() {
		cacheContexts();
		contextLeaseTimeStamp = System.currentTimeMillis();
	}

	private void cacheContexts() {
		try {
			contexts = api.getContexts(auth);
		} catch (ToodledoApiException e) {
			handleApiException(ToodledoErrors.ERROR_REQUESTING_CONTEXTS, e);
		}
	}

	private boolean shouldRequestNewContextList() {
		return contexts == null || System.currentTimeMillis() - contextLeaseTimeStamp > leaseMinutes * 60 * 1000;
	}

	private void applyStatus(final ToodledoPimItem item, Todo task, final String statusString) {
		Arrays.stream(Status.values())
		//
				.filter(status -> status.name().startsWith(statusString.toUpperCase()))//
				.forEach(status -> {
					task.setStatus(status);
					item.setStatus(status.name());
				});
	}

	private Todo prepareTask(final ToodledoPimItem item) {
		Todo task = new Todo();
		task.setTitle(item.getTitle());
		task.setNote(item.getNote());
		task.setPriority(Priority.ValueFromInt.get(item.getPriority()));
		task.setStar(item.isStarred());
		String tagString = item.getTags().toString();
		tagString = tagString.substring(1, tagString.length() - 1);
		task.setTag(tagString);
		return task;
	}

	private ToodledoPimItem convertToodledoTaskToPimItem(Todo task) {
		ToodledoPimItem item = new ToodledoPimItem();
		item.setStartTime(task.getStartbefore());
		item.setDueTime(task.getEnd());
		item.setDueDate(task.getEnd());
		String tagString = getCharCodedStr(task.getTag());
		if (tagString != null) {
			List<String> tags = Arrays.asList(this.getCharCodedStr(task.getTag()).split(","));
			for (int i = 0; i < tags.size(); i++) {
				tags.set(i, tags.get(i).trim());
			}
			// TextHatchLogger.log(tags);
			item.setTags(tags);
			tagSet.addAll(tags);
		}
		item.setTitle(getCharCodedStr(task.getTitle()));
		// TextHatchLogger.log(item.getTitle());
		item.setContextId(task.getContext());
		return item;
	}

	private String getCharCodedStr(String str) {
		char[] array = new char[str.length()];
		try {
			new InputStreamReader(new StringBufferInputStream(str), Charset.forName("UTF8")).read(array);
		} catch (IOException ex) {
			Logger.getLogger(ToodledoPimApi.class.getName()).log(Level.SEVERE, null, ex);
		}
		return new String(array);
	}

	@Override
	public List<ToodledoPimItem> getEventList() {
		List<Todo> tasks = getTasks();
		List<ToodledoPimItem> items = new ArrayList<>();
		for (Todo task : tasks) {
			items.add(convertToodledoTaskToPimItem(task));
		}
		return items;
	}

	public List<ToodledoPimItem> getEventList(String folder) throws ToodledoApiException {
		List<Todo> tasks = (folder != null) ? getTasks(folder) : getTasks();
		List<ToodledoPimItem> items = new ArrayList<>();
		for (Todo task : tasks) {
			items.add(convertToodledoTaskToPimItem(task));
		}
		return items;
	}

	private Optional<Folder> getFolder(String projectString) {
		cacheFolders();
		return folders.stream()//
				.filter(folder -> folder.getSName().toLowerCase().startsWith(projectString.toLowerCase()))//
				.findAny();
	}

	private void cacheFolders() {
		try {
			folders = api.getFolders(auth);
		} catch (ToodledoApiException e) {
			handleApiException(ToodledoErrors.ERROR_REQUESTING_FOLDERS, e);
		}
	}

	@Override
	public String name() {
		return "ToodleDo.com";
	}

	public TreeSet<String> getTags(ArrayList<Todo> tasks) {
		DevsLogger.log("includes " + tagIncludeSet);
		DevsLogger.log("exclude " + tagExcludeSet);
		tagText = "";
		TreeSet<String> tags = new TreeSet<>();
		taskCount = 0;
		for (Todo task : tasks) {
			z: if (task.getTag() != null) {
				String tagString = getCharCodedStr(task.getTag());
				for (String skips : tagExcludeSet) {
					if (!skips.equals("") && tagString.contains(skips.trim())) {
						// TextHatchLogger.log("skipping tag \"" + skips +
						// "\"");
						break z;
					}
				}
				boolean found = tagIncludeSet.isEmpty();
				if (!found) {
					for (String str : tagIncludeSet) {
						if (tagString.contains(str.trim())) {
							found = true;
						}
					}
					if (!found) {
						break z;
					}
				}
				List<String> tagList = Arrays.asList(tagString.split(","));
				for (int i = 0; i < tagList.size(); i++) {
					tagList.set(i, tagList.get(i).trim());
					tagText += " " + tagList.get(i);
				}
				tags.addAll(tagList);
				taskCount++;
			}
		}
		tags.remove("");
		DevsLogger.log(tags);
		return tags;
	}

	public List<Todo> getTasks() {
		try {
			return api.getTodosList(auth);
		} catch (ToodledoApiException e) {
			handleApiException(ToodledoErrors.ERROR_REQUESTING_FOLDERS, e);
		}
		return Collections.EMPTY_LIST;
	}

	public List<Todo> getTasks(String folderStr) {
		try {
			Folder folder = requestFolder(folderStr);
			Todo filter = new Todo();
			filter.setFolder(folder.getId());
			return api.getTodosList(auth, filter);
		} catch (ToodledoApiException e) {
			handleApiException(ToodledoErrors.ERROR_REQUESTING_FOLDERS, e);
		}
		return Collections.EMPTY_LIST;
	}

	private Folder requestFolder(String folderStr) throws ToodledoApiException {
		Optional<Folder> optionalFolder = getFolder(folderStr);
		if (!optionalFolder.isPresent())
			handleError(ToodledoErrors.FOLDER_NOT_FOUND);
		return optionalFolder.get();
	}

	private void handleError(ToodledoErrors folderNotFound) {
	}

	private void handleApiException(ToodledoErrors folderNotFound, ToodledoApiException e) {
		// try {
		// authenticator().authenticate();
		// } catch (AuthenticationException e1) {
		// DevsLogger.log(e1);
		// }
	}

	public void tag2Folder(String tag, String folderStr, ArrayList<Todo> tasks) throws ToodledoApiException {
		cacheFolders();
		Folder folder = null;
		for (Folder f : folders) {
			if (folderStr.equals(f.getSName())) {
				folder = f;
				DevsLogger.log("found folder " + f.getSName());
				break;
			}
		}
		if (folder == null) {
			return;
		}
		for (Todo task : tasks) {
			if (task.getTag() != null && task.getTag().contains(tag)) {
				task.setFolder(folder.getId());
				api.modifyTodo(auth, task);
				DevsLogger.log("moved " + task.getTitle() + " in folder " + folder.getSName());
			}
		}

	}

	@Override
	protected ItemLineParser createLineParser(LineAndNote textHatch) {
		return new ToodledoLineParser(textHatch);
	}

	@Override
	public void deleteItem(ToodledoPimItem item) {
	}

	public static void computeDateTime(ToodledoPimItem item, String dueDateFragment) {
		DevsLogger.log("parsing date: " + dueDateFragment);
		Date[] date = getDateTimeFromString(item, dueDateFragment);
		item.setDueDate(dueDateFragment.length());
		if (date[0] != null && date[0].getTime() > 1000 * 60 * 60 * 24) {
			// item.setStartTime(date[0]);
			// item.setDateValid(true);
			// if (date[1] != null) {
			// item.setDueTime(date[1]);
			// }
		}
	}

	private static Date[] getDateTimeFromString(ToodledoPimItem item, String due) {
		String[] dateTime = due.split(Symbols.SYMBOL_DATE_TIME_SPLITTER);
		Date[] parsedDates = new Date[2];
		long[] parsedTimes = new long[2];
		Date startDate = new Date(0);
		Date endDate = new Date(0);
		long endTime = 0;
		long startTime = 0;
		if (dateTime.length == 2) {
			parsedTimes = getTimes(dateTime[1].split(Symbols.SYMBOL_UNTIL));
			startTime = parsedTimes[0];
			DevsLogger.log("startTime is " + startTime / (60 * 1000 * 60) + " hours");
			endTime = parsedTimes[1];
			DevsLogger.log("endTime is " + endTime / (60 * 1000 * 60) + " hours");
			// String[] times = dateTime[1].split(until);
			// startTime = getTimeFromString(times[0]);
			// if (times.length == 2) {
			// endTime = getTimeFromString(times[1]);
			// }
		}
		item.setAllDay(dateTime.length != 2);
		DevsLogger.log("allDay is " + item.isAllDay());
		String[] date = dateTime[0].split(Symbols.SYMBOL_UNTIL);
		parsedDates = getDatesFromString(date);
		startDate = parsedDates[0];
		DevsLogger.log("startDate is " + startDate);
		endDate = parsedDates[1];
		DevsLogger.log("endDate is " + endDate);
		if (item.isAllDay()) {
			startTime = 0;
			endTime = 24 * 1000 * 60 * 60;
		}
		if (endDate.getTime() < startDate.getTime()) {
			System.out.print("endDate<startDate, converting to ");
			endDate = new Date(startDate.getTime() + endTime);
			DevsLogger.log(endDate);
		} else {
			endDate = new Date(endDate.getTime() + endTime);
			DevsLogger.log("endDate with Time is " + endDate);
		}
		if (startDate.getTime() != 0) {
			startDate = new Date(startDate.getTime() + startTime);
			DevsLogger.log("startDate with Time is " + startDate);
		}
		DevsLogger.log("# " + startDate + " - " + endDate);
		return new Date[] { startDate, endDate };
	}

	private static long getTimeFromString(String dueTimeString) {
		Date date = null;
		for (int i = 0; i < timePatterns.length; i++) {
			try {
				DateFormat df = new SimpleDateFormat(timePatterns[i], (Locale) DI.get(CORE.LOCALE));
				df.setTimeZone(TimeZone.getTimeZone("GMT"));
				date = df.parse(dueTimeString);
			} catch (ParseException ex) {
				continue;
			}
			if (date != null) {
				break;
			}
		}
		DevsLogger.log("timeParsing results " + date);
		return date.getTime();
	}

	private static long[] getTimes(String[] split) {
		long[] array = new long[2];
		for (int i = 0; i < split.length; i++) {
			array[i] = getTimeFromString(split[i]);
		}
		return array;
	}

	public static String[] datePatterns = { "dd.MM.", "dd.MMM", "dd", "EE", "dd.MM" };

	public static String[] timePatterns = { "HH:mm", "HH" };

	private static Date[] getDatesFromString(String... dateArray) {
		Calendar cal = Calendar.getInstance();
		Date start = new Date(0);
		Date end = new Date(0);
		boolean isMonthIn2ndPossible = false;
		for (int j = 0; j < dateArray.length; j++) {
			String dateStr = dateArray[j];
			Date date = (j == 0) ? start : end;
			if (dateStr.startsWith(Symbols.SYMBOL_TODAY)) {
				date = todayZero(Calendar.getInstance().getTime()).getTime();
			} else if (dateStr.startsWith(Symbols.SYMBOL_TOMORROW)) {
				date = todayZero(new Date(Calendar.getInstance().getTime().getTime() + (24 * 60 * 60 * 1000)))
						.getTime();
			} else {
				for (int i = 0; i < datePatterns.length; i++) {
					try {
						String pattern = datePatterns[i];
						DateFormat df = new SimpleDateFormat(pattern, (Locale) DI.get(CORE.LOCALE));
						cal.setTime(df.parse(dateStr));
						if (pattern.equals("EE")) {
							Date searchStart = (date == end) ? start : Calendar.getInstance().getTime();
							cal.setTime(searchNextWeekDay(cal.get(Calendar.DAY_OF_WEEK), searchStart));
						} else {
							if (!pattern.contains("y")) {
								cal.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
							}
							if (pattern.contains("M")) {
								if (isMonthIn2ndPossible && date == end) {
									Calendar cal2 = Calendar.getInstance();
									cal2.setTime(start);
									cal2.set(Calendar.MONTH, cal.get(Calendar.MONTH));
									start = cal2.getTime();
								}
							} else if (!pattern.contains("M")) {
								if (dateArray.length == 2 && date == start) {
									isMonthIn2ndPossible = true;
								}
								cal.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
							}
						}
					} catch (ParseException ex) {
						continue;
					}
					date = todayZero(cal.getTime()).getTime();
					break;
				}
			}
			DevsLogger.log("date is " + date);
			if (j == 0) {
				start = date;
			} else {
				end = date;
			}
		}
		return new Date[] { start, end };
	}

	private static Date searchNextWeekDay(int weekDay, Date startDate) {
		DevsLogger.log("searching next " + weekDay + ". day of week from " + startDate);
		Calendar cal = todayZero(startDate);
		long now = cal.getTimeInMillis();
		int startDay = cal.get(Calendar.DAY_OF_WEEK);
		DevsLogger.log(weekDay);
		if (weekDay < startDay) {
			weekDay += 7;
		}
		long plusDays = (weekDay - startDay) * 24 * 60 * 60 * 1000;
		return new Date(now + plusDays);
	}

	public static Calendar todayZero(Date startDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(startDate);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}

}
