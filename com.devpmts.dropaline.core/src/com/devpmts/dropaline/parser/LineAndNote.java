package com.devpmts.dropaline.parser;

import com.devpmts.util.StringUtil;

public class LineAndNote {

	public LineAndNote(String titleText, String noteText) {
		this.line = titleText.trim();
		this.note = noteText.trim();
	}

	public String line = StringUtil.EMPTY_STRING;

	public String note = StringUtil.EMPTY_STRING;

	public boolean hasTitle() {
		return line.isEmpty();
	}
}