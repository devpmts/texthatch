package com.dropaline.plugins.apis.toodleDo;

import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_CALENDAR_DATE;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_CONTEXT;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_DUE_DATE;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_PRIORITY;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_PROJECT_SYMBOL;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_STARRED;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_STATUS;
import static com.dropaline.plugins.apis.toodleDo.Symbols.SYMBOL_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.StringUtil;

// imcom.devpmts.dropaline.corealine.core.api.PimApcom.devpmts.textHatch.dropaline.core.parser.ItemLinecom.devpmts.dropaline.corevpmts.dropaline.core.parser.TextHatch;

public class ToodledoLineParser extends ItemLineParser<ToodledoPimItem> {

	@Inject
	private UserInterface userInterface;

	public ToodledoLineParser(LineAndNote textHatch) {
		super(textHatch, Symbols.BREAKING_SYMBOLS);
	}

	@Override
	public ToodledoPimItem parseLine() {
		pimItem = new ToodledoPimItem();
		pimItem.setNote(lineAndNote.note);
		pimItem.setStarred(isStarred());
		pimItem.setPriority(getNumberOfExclamationMarks());
		pimItem.setTags(cutTags(pimItem));
		pimItem.setStatus(getOneWordFragment(SYMBOL_STATUS));
		pimItem.setContext(getOneWordFragment(SYMBOL_CONTEXT));
		pimItem.setProject(getOneWordFragment(SYMBOL_PROJECT_SYMBOL));
		pimItem = extractDateTime(pimItem);
		pimItem.setTitle(lineAndNote.line);
		return pimItem;
	}

	private int getNumberOfExclamationMarks() {
		String marks = regexDSL.EMBEDDED_OR_AT_END(regexDSL.ONE_TO_THREE_TIMES(SYMBOL_PRIORITY));
		return cutOut(marks).length();
	}

	private boolean isStarred() {
		if (cutOut(regexDSL.EMBEDDED_OR_AT_END(SYMBOL_STARRED)).length() == 1) {
			return true;
		}
		if (lineAndNote.line.startsWith(SYMBOL_STARRED)) {
			lineAndNote.line = lineAndNote.line.substring(1);
			return true;
		}
		return false;
	}

	private Set<String> computeTags(ArrayList<String> tagParts) {
		Set<String> set = new TreeSet<>();
		String possibleTitle = StringUtil.EMPTY_STRING;
		List<String> tmpTagList = new ArrayList<>();
		for (String tagPart : tagParts) {
			String[] tagArray = tagPart.split(",");
			int lastIndex = tagArray.length - 1;

			tagArray[0] = ItemLineParser.withoutSymbol(tagArray[0], SYMBOL_TAG);

			possibleTitle = tagArray[lastIndex].trim();
			String[] lastArr = possibleTitle.split(" ");
			DevsLogger.log(Arrays.toString(lastArr));
			tagArray[lastIndex] = lastArr[0];
			if (lastArr.length > 1) {
				possibleTitle = possibleTitle.substring(tagArray[lastIndex].length());
			} else {
				possibleTitle = StringUtil.EMPTY_STRING;
			}
			DevsLogger.log(tagArray[lastIndex]);
			tmpTagList.addAll(Arrays.asList(tagArray));
		}
		for (String str : tmpTagList) {
			str = str.trim();
			if (str.length() == 0) {
				continue;
			}
			set.add(str);
		}
		DevsLogger.log("taglist:  " + set);
		lineAndNote.line += " " + possibleTitle;
		cutOutMatches(tagParts);
		DevsLogger.log("set of tags is " + set);
		return set;
	}

	private Set<String> cutTags(ToodledoPimItem item) {
		ArrayList<String> matches = find(regexDSL.SYMBOL_AND_COMMA_SEPARATED_LIST(SYMBOL_TAG));
		if (matches.isEmpty()) {
			return Collections.EMPTY_SET;
		}
		return computeTags(matches);
	}

	private ToodledoPimItem extractDateTime(ToodledoPimItem item) {
		String dateString = regexDSL.SYMBOL_AND_DATELIKE_STRING(SYMBOL_DUE_DATE);
		String dueDateFragment = getFragment(dateString, SYMBOL_DUE_DATE);
		if (dueDateFragment.isEmpty()) {
			String calendarString = regexDSL.SYMBOL_AND_DATELIKE_STRING(SYMBOL_CALENDAR_DATE);
			dueDateFragment = getFragment(calendarString, SYMBOL_CALENDAR_DATE);
		}
		if (dueDateFragment.isEmpty())
			return item;

		ToodledoPimApi.computeDateTime(item, dueDateFragment);
		return item;
	}

	@Override
	public void doBeforeParsing() {
		// if (isTagCloudSymbolPresent()) {
		// changeTags.getTagCloud(userInterface.getTextLine());
		// return;
		// }
	}

	// @Inject
	// private ChangeTag changeTags;

	private boolean isTagCloudSymbolPresent() {
		return userInterface.getTextLine().startsWith(Symbols.SYMBOL_TAG_CLOUD);
	}

}
