package com.textHatch.plugins.apis.catchNotes;

import com.catchnotes.api.CatchAPI;
import com.devpmts.dropaline.api.Authenticator;

public class CatchAuthenticator extends Authenticator {

	// private CatchAPI catchApi;

	public CatchAuthenticator(CatchAPI api) {
		// this.catchApi = api;
	}

	@Override
	public void authenticate() {
		// TextHatchLogger.log("authenticating Catch");
		// String token = PropManager.getProp("NOTE_token");
		// CatchAccount acc = new CatchAccount();
		// int i = 0;
		// if (token != null) {
		// api.setAccessToken(token);
		// }
		// i = api.signIn(PropManager.getProp("NOTE_email"),
		// PropManager.getProp("NOTE_password"), acc);
		// if (i == 1) {
		authenticated = true;
		// } else {
		// TextHatchLogger.log("inputFailed");
		// PropManager.control.loginFailed(EventType.NOTE);
		// }
	}

}
