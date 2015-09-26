package com.devpmts.dropaline.textLine;

import java.io.Serializable;

class TitleAndNote implements Serializable {

	String title = "";

	String note = "";

	public String getNote() {
		return note;
	}

	public String getTitle() {
		return title;
	}

	public void setNote(String notes) {
		this.note = notes;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}