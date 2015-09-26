package com.devpmts.dropaline.parser;

public class THException extends Exception {

	public THException(Exception ex) {
		super(ex);
	}

	public THException(String string) {
		super(string);
	}

}
