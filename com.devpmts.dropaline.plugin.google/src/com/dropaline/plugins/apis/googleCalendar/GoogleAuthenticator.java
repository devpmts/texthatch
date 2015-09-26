package com.dropaline.plugins.apis.googleCalendar;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.api.AuthenticationException;
import com.devpmts.dropaline.api.Authenticator;
import com.google.api.services.calendar.Calendar;

public class GoogleAuthenticator extends Authenticator {

	Calendar service;

	public GoogleAuthenticator(Calendar service) {
		this.service = service;
	}

	@Override
	public void authenticate() throws AuthenticationException {
		try {
			DevsLogger.log("authentication google Calendar");
			authenticate(service);
			authenticated = true;
			// ((GoogleCalendarPimApi) pimApi()).storeContacts();
		} catch (Exception e) {
			throw new AuthenticationException(e.getMessage());
		}
	}

	protected void authenticate(Calendar service) {
		// service.UserCredentials(userName(), password());
	}
}
