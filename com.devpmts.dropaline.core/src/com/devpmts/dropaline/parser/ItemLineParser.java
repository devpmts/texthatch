package com.devpmts.dropaline.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.devpmts.dropaline.textLine.PimItem;

public abstract class ItemLineParser<PIMITEM extends PimItem> {

	protected LineAndNote lineAndNote;

	protected PIMITEM pimItem;

	protected RegexDSL regexDSL;

	protected ItemLineParser(LineAndNote textHatch, String breakingSymbols) {
		this.lineAndNote = textHatch;
		regexDSL = new RegexDSL(breakingSymbols);
	}

	public abstract PIMITEM parseLine();

	public LineAndNote getLineAndNote() {
		return lineAndNote;
	}

	protected String getOneWordFragment(String symbol) {
		String str = regexDSL.SYMBOL_AND_ONE_WORD(symbol);
		String fragment = getFragment(str, symbol);
		return fragment;
	}

	protected String cutOut(String regex) {
		ArrayList<String> results = find(regex);
		int index = results.size() - 1;
		if (index < 0) {
			return "";
		}
		String result = results.get(index);
		cutOutString(result);
		return result.trim();
	}

	protected String getFragment(String regex, String symbol) {
		String match = cutOut(regex);
		if (match.startsWith(symbol)) {
			match = match.substring(symbol.length());
		}
		return match.trim();
	}

	protected void cutOutString(String str) {
		int start = lineAndNote.line.indexOf(str);
		int end = start + str.length();
		lineAndNote.line = lineAndNote.line.substring(0, start)
				+ lineAndNote.line.substring(end, lineAndNote.line.length());
	}

	protected ArrayList<String> find(String regex) {
		return findAll(regex, lineAndNote.line);
	}

	private ArrayList<String> findAll(String regex, String str) {
		CharSequence sequence = str.subSequence(0, str.length());
		Matcher matcher = Pattern.compile(regex).matcher(sequence);
		ArrayList<String> results = new ArrayList<>();
		while (matcher.find()) {
			results.add(matcher.toMatchResult().group().trim());
		}
		return results;
	}

	protected void cutOutMatches(ArrayList<String> matches) {
		for (String match : matches) {
			cutOutString(match);
		}
	}

	public static String withoutSymbol(String text, String symbol) {
		return text.substring(symbol.length());
	}

	public void doBeforeParsing() {
	}

}
