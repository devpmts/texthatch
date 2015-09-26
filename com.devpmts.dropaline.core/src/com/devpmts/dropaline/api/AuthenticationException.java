package com.devpmts.dropaline.api;

public class AuthenticationException extends Exception {

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Exception ex) {
		super(message, ex);
		System.err.println(ex.getMessage());
	}

}
