package com.devpmts.dropaline.parser;

import java.util.regex.Pattern;

public class RegexDSL {

	private final String CUT_HERE_SYMBOLS;

	private final String WHITESPACE = RegexDSL.quote(" ");

	private final String WORD = RegexDSL.quote("/w");

	private final String OR = "|";

	public RegexDSL(String cutHereSymbols) {
		CUT_HERE_SYMBOLS = quote(cutHereSymbols);
	}

	private String REGEX_UNTIL_BREAK() {
		return "[^" + CUT_HERE_SYMBOLS + "]";
	}

	private String REGEX_WORD_UNTIL_BREAK() {
		return "[." + "[^" + WHITESPACE + CUT_HERE_SYMBOLS + "]" + "]++";
	}

	private String AT_THE_END(String oneToThreeTimes) {
		return "^" + quote(oneToThreeTimes);
	}

	public String SYMBOL_AND_ONE_WORD(String symbol) {
		return quote(symbol) + REGEX_WORD_UNTIL_BREAK();
	}

	public String SYMBOL_EMBEDDED_IN_WHITE_SPACE(String symbol) {
		return WHITESPACE + quote(symbol) + WHITESPACE;
	}

	public String EMBEDDED_OR_AT_END(String symbol) {
		return SYMBOL_EMBEDDED_IN_WHITE_SPACE(symbol) + OR + AT_THE_END(symbol);
	}

	public String ONE_TO_THREE_TIMES(String symbol) {
		return quote(symbol) + "{1,3}+";
	}

	public String SYMBOL_AND_DATELIKE_STRING(String symbol) {
		return quote(symbol) + "[.++" + REGEX_UNTIL_BREAK() + "]++";
	}

	public String SYMBOL_AND_COMMA_SEPARATED_LIST(String symbol) {
		return quote(symbol) + "[" + WORD + "++" + "," + WORD + "++" + WHITESPACE + REGEX_UNTIL_BREAK() + "]++";
	}

	public static String quote(String str) {
		return Pattern.quote(str);
	}
}