package com.dropaline.plugins.apis.googleCalendar;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.parser.THException;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;

@Singleton
@Creatable
public class GoogleCalendarPimApi extends PimApi<GoogleAuthenticator, GoogleCalendarPimItem> {

	public static boolean useContacts = true;

	static {
		System.err.println("we're happy");
	}

	// public void syncWithContactNote(ToodledoPimItem item) {
	// if (useContacts) {
	// for (String tag : item.getTags()) {
	// tryAddNoteToContact(item.getTitle(), item.getNote(), tag);
	// }
	// }
	// }
	//
	// public static void syncCal2Task() {
	// ToodledoPimApi taskPimApi = DI.get(ToodledoPimApi.class);
	// GoogleCalendarPimApi calendarPimApi = DI.get(GoogleCalendarPimApi.class);
	// Calendar calendar = Calendar.getInstance();
	// Date date = new Date(calendar.getTimeInMillis() + 360000 * 24 * 7);
	// calendarPimApi.getEventList(calendar.getTime(), date);
	// List<ToodledoPimItem> listTask = taskPimApi.getEventList("calendar");
	// for (ToodledoPimItem item : listTask) {
	// if (item.getDueTime().getTime().getTime() <
	// ThDateParser.todayZero(calendar.getTime()).getTime().getTime()) {
	// taskPimApi.deleteItem(item);
	// }
	// }
	// }

	public static Map<String, ContactEntry> contactMap = new HashMap<>();

	public static String[] contactNames = new String[] {};

	// static Calendar service = new Calendar(null, null, null);

	URL calendarFeed;

	String calendarsSuffix = "/owncalendars/full";

	String contactUrlPrefix = "https://www.google.com/m8/feeds/contacts/";

	String feedPrefix = "https://www.google.com/calendar/feeds/";

	String privateSuffix = "/private/full";

	private boolean quickAdd = false;

	private boolean sendNotifications = true;

	URL userFeed;

	@Inject
	public GoogleCalendarPimApi() {
		super(new GoogleAuthenticator(null));
		System.err.println("creating google api");
	}

	public void addNoteToContact(String name, String info, boolean remove) {
		// for (String contactName : contactNames) {
		// if (contactName.startsWith(name)) {
		// if (contactMap.values().contains(null)) {
		// PropManager.set("CALENDAR_contactUpdateTime", 0);
		// storeContacts();
		// }
		DevsLogger.log(contactMap);
		ContactEntry contact = contactMap.get(name);
		String content = "";
		// if ((contact.getContent()) != null) {
		// content = contact.getTextContent().getContent().getPlainText();
		// }
		if (content.contains(info)) {
			if (remove) {
				content = content.replace("\n* " + info, "");
				return;
			}
			return;
		}
		DevsLogger.log(content);
		if (!content.contains("TASKS:")) {
			content += "\n" + " TASKS: " + "\n";
		}
		content += "* " + info + "\n";
		// contact.setContent(new PlainTextConstruct(content));
		// DevsLogger.log("contact is prepared with " +
		// contact.getTextContent().getContent().getPlainText());
		try {
			ContactsService service = new ContactsService("exampleCo-exampleApp-1");
			authenticator().authenticate();
			new URL(contactUrlPrefix + "default/full");
			DevsLogger.log("inserting...");
			// contact.update();
			// ContactEntry success = service.update(contactUrl, contact);
			DevsLogger.log("success is");
		} catch (Exception ex) {
			Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE, null, ex);
		}
		// }
		// }
	}

	@Override
	public void deliverPimItem(GoogleCalendarPimItem item) throws THException {
		DevsLogger.log("connecting google event...");
		// try {
		// updateURLs();
		// String context = item.getContext();
		// if (context.length() > 0) {
		// // CalendarFeed calFeed = service.getFeed(calendarFeed,
		// CalendarFeed.class);
		// for (int i = 0; i < calFeed.getEntries().size(); i++) {
		// CalendarEntry calEntry = calFeed.getEntries().get(i);
		// if (calEntry.getTitle().getPlainText().equals(context)) {
		// DevsLogger.log((calEntry.getId().split("calendars/")[1]));
		// userFeed = new URL(feedPrefix +
		// calEntry.getId().split("calendars/")[1] + privateSuffix);
		// }
		// }
		// }
		// CalendarEventEntry timeEvent = new CalendarEventEntry();
		// When when = null;
		// if (quickAdd) {
		// timeEvent.setQuickAdd(true);
		// timeEvent.setContent(new PlainTextConstruct(item.getDueDate()));
		// } else {
		// when = new When();
		// long daySpan = 24 * 60 * 60 * 1000;
		// long hourSpan = 60 * 1000 * 60;
		// long startM = item.getStartTime().getTime().getTime();
		// long endM = item.getDueTime().getTime().getTime();
		// endM = (endM < startM) ? (item.isAllDay()) ? startM + daySpan :
		// startM + hourSpan
		// : (item.isAllDay()) ? endM + daySpan : endM;
		// startM = (item.isAllDay()) ? startM + daySpan : startM;
		// DateTime startDate = new DateTime(new Date(startM),
		// TimeZone.getDefault());
		// DateTime endDate = new DateTime(new Date(endM),
		// TimeZone.getDefault());
		// if (item.isAllDay()) {
		// startDate.setDateOnly(true);
		// endDate.setDateOnly(true);
		// }
		// when.setStartTime(startDate);
		// when.setEndTime(endDate);
		// timeEvent.addTime(when);
		// }
		// DevsLogger.log(timeEvent);
		// if ((boolean) DI.get(CORE.LEARNING_MODE)) {
		// return;
		// }
		// timeEvent.setSendEventNotifications(sendNotifications);
		// timeEvent = service.insert(userFeed, timeEvent);
		// timeEvent.setTitle(new PlainTextConstruct(item.getTitle()));
		// timeEvent.setContent(new PlainTextConstruct(item.getNote()));
		// DevsLogger.log(timeEvent.getTimes().get(0).getStartTime().toUiString());
		// timeEvent.update();
		// } catch (IOException | ServiceException ex) {
		// throw new THException(ex);
		// }
	}

	// public GoogleCalendarPimItem
	// convertAppointmentToPimItem(CalendarEventEntry entry) {
	// GoogleCalendarPimItem item = new GoogleCalendarPimItem();
	// item.setTitle(entry.getTitle().getPlainText());
	// When when = entry.getTimes().get(0);
	// item.setStartTime(new Date(when.getStartTime().getValue()));
	// item.setDueTime(new Date(when.getEndTime().getValue()));
	// return item;
	// }

	@Override
	public void deleteItem(GoogleCalendarPimItem item) {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	// public Map<String, ContactEntry> getContacts() {
	// try {
	// DevsLogger.log("trying to get Contacts");
	// ContactsService service = new ContactsService("exampleCo-exampleApp-1");
	// authenticator().authenticate(service);
	// URL contactUrl = new URL(contactUrlPrefix + "default/full");
	// DevsLogger.log(contactUrl);
	// ContactFeed resultFeed = service.getFeed(contactUrl, ContactFeed.class);
	// HashMap<String, ContactEntry> map = new HashMap<>();
	// for (ContactEntry entry : resultFeed.getEntries()) {
	// if (entry.getNickname() != null) {
	// DevsLogger.log(entry.getNickname().getValue());
	// map.put(entry.getNickname().getValue(), entry);
	// }
	// }
	// return map;
	// } catch (IOException ex) {
	// Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE,
	// null, ex);
	// } catch (ServiceException ex) {
	// Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE,
	// null, ex);
	// }
	// return null;
	// }

	@Override
	public List<GoogleCalendarPimItem> getEventList() {
		// return getEventList(null, null);
		return null;
	}

	// public List<GoogleCalendarPimItem> getEventList(Date start, Date end) {
	// try {
	// updateURLs();
	//
	// CalendarQuery myQuery = new CalendarQuery(userFeed);
	// // myQuery.setMinimumStartTime(DateTime.now());
	// // myQuery.setMaximumStartTime(new
	// // DateTime(System.currentTimeMillis() + (1000 * 60 * 60 * 24 *
	// // 30)));
	// if (start != null && end != null) {
	// myQuery.setMinimumStartTime(new DateTime(start));
	// myQuery.setMaximumStartTime(new DateTime(end));
	// }
	//
	// CalendarEventFeed resultFeed = service.query(myQuery,
	// CalendarEventFeed.class);
	// List<GoogleCalendarPimItem> items = new ArrayList<>();
	// for (CalendarEventEntry entry : resultFeed.getEntries()) {
	// items.add(convertAppointmentToPimItem(entry));
	// }
	// } catch (IOException ex) {
	// Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE,
	// null, ex);
	// } catch (ServiceException ex) {
	// Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE,
	// null, ex);
	// }
	// return null;
	// }

	@Override
	public String name() {
		return "Google Calendar";
	}

	public void storeContacts() {
		getProperty("contactUpdateTime", 0);
		// if (time == null || System.currentTimeMillis() - Long.parseLong(time)
		// > 7 * 24 * 60 * 60 * 1000) {
		// contactMap = getContacts();
		String str = contactMap.keySet().toString();
		str = str.substring(1, str.length() - 1);
		DevsLogger.log("storing Contacts Nicknames in Properties");
		setProperty("nickNames", str);
		setProperty("contactUpdateTime", System.currentTimeMillis());
		// }
		contactNames = getProperty("nickNames", "").split(",");
	}

	public void updateURLs() {
		try {
			String email = authenticator().userName();
			DevsLogger.log("email is " + email);
			userFeed = new URL(feedPrefix + email + privateSuffix);
			calendarFeed = new URL(feedPrefix + email + calendarsSuffix);
		} catch (MalformedURLException ex) {
			Logger.getLogger(GoogleCalendarPimApi.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public void tryAddNoteToContact(String title, String note, String contactName) {
		// TODO check contact exists
		for (String name : contactMap.keySet()) {
			if (name.startsWith(contactName)) {
				DevsLogger.log("found Contact: " + name);
				addNoteToContact(name, title + "\n" + note, false);
			}
		}

	}

	@Override
	protected ItemLineParser createLineParser(LineAndNote textHatch) {
		return null;
	}

}
