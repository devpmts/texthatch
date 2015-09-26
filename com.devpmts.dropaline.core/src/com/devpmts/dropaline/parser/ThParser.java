package com.devpmts.dropaline.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;

import com.devpmts.dropaline.delivery.ItemDelivery;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.StringUtil;
import com.devpmts.util.e4.DI;

public class ThParser {

	public static String SYMBOL_TITLE_SEPARATOR = "|";

	private ItemLineParser lineParser;

	private LineAndNote lineAndNote;

	@Inject
	@Optional
	private UserInterface userInterface;

	public ThParser(ItemLineParser lineParser) {
		this.lineParser = lineParser;
		this.lineAndNote = lineParser.getLineAndNote();
		DI.injectContextIn(this);
	}

	protected boolean isListOfItemsInNoteTextArea() {
		return lineAndNote.line.startsWith(ThParser.SYMBOL_TITLE_SEPARATOR);
	}

	protected List<String> getListOfItems() {
		List<String> strings = getItemLinesSeparatedByLineFeed(lineAndNote.note);
		lineAndNote.note = StringUtil.EMPTY_STRING;
		return strings;
	}

	protected List<PimItem> parseAllItems(List<String> strings) {
		return strings.stream().map(taskLine -> {
			return lineParser.parseLine();
		}).collect(Collectors.toList());
	}

	protected void transmitAllItems(List<PimItem> items) {
		boolean successfullySentAllItems = ItemDelivery.attemptToSendAllItems(items);
		if (!successfullySentAllItems) {
			return;
		}
		userInterface.handleSuccessfulDelivery(items);
	}

	private List<String> getItemLinesSeparatedByLineFeed(String strings) {
		return Arrays.asList(strings.trim().split("\n"));
	}

	public void parse() {
		List<String> strings = new ArrayList<String>();
		if (isListOfItemsInNoteTextArea()) {
			strings = getListOfItems();
		} else {
			strings.add(lineAndNote.line);
		}
		lineParser.doBeforeParsing();
		List<PimItem> parsedItems = parseAllItems(strings);
		transmitAllItems(parsedItems);
	}

}
