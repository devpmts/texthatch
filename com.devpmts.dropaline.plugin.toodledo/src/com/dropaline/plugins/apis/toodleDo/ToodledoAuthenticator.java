package com.dropaline.plugins.apis.toodleDo;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.loststone.toodledo.ToodledoApi;
import org.loststone.toodledo.exception.IncorrectUserPasswordException;
import org.loststone.toodledo.exception.MissingPasswordException;
import org.loststone.toodledo.exception.ToodledoApiException;
import org.loststone.toodledo.util.AuthToken;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.AuthenticationException;
import com.devpmts.dropaline.api.Authenticator;
import com.devpmts.util.e4.DI;

public class ToodledoAuthenticator extends Authenticator {

	private static final String USER_ID = "userId";

	public static final String TOODLEDO_AUTHENTICATION_TOKEN = "toodledo_authentication_token";

	private static final String TOKEN_TIME = "token_time";

	private ToodledoApi toodledoApi;

	@Inject
	@Optional
	@Named(TOODLEDO_AUTHENTICATION_TOKEN)
	AuthToken authToken;

	@Inject
	@Optional
	private void setToken(
			@Preference(nodePath = CORE.NODE_PATH, value = TOODLEDO_AUTHENTICATION_TOKEN + "String") String toodledoToken, //
			@Preference(nodePath = CORE.NODE_PATH, value = USER_ID) String userId) {
		if (authToken == null && toodledoToken != null) {
			AuthToken authToken = new AuthToken(password(), userId, toodledoToken);
			DI.set(TOODLEDO_AUTHENTICATION_TOKEN, authToken);
			authenticated = true;
		}
	}

	public ToodledoAuthenticator(ToodledoApi api) {
		toodledoApi = api;
	}

	@Override
	public void authenticate() throws AuthenticationException {
		String userId = requestToodledoUserId();
		requestToodledoToken(userId);
	}

	private boolean isSessionValid() {
		return authToken != null && authToken.getRemainingTime() < 1;
	}

	@Override
	public boolean isAuthenticated() {
		if (!isSessionValid()) {
			authenticated = false;
		}
		return authenticated;
	}

	private String requestToodledoUserId() throws AuthenticationException {
		try {
			String userId = toodledoApi.getUserId(userName(), password());
			DI.setPreference(USER_ID, userId);
			return userId;
		} catch (ToodledoApiException | IncorrectUserPasswordException | MissingPasswordException e) {
			throw new AuthenticationException(e.getMessage());
		}
	}

	private void requestToodledoToken(String userId) throws AuthenticationException {
		try {
			AuthToken token = toodledoApi.initialize(userId, password());
			DI.set(TOODLEDO_AUTHENTICATION_TOKEN, token);
			DI.setPreference(TOODLEDO_AUTHENTICATION_TOKEN + "String", token.getToken());
			DI.setPreference(TOKEN_TIME, token.getDate().toString());
		} catch (ToodledoApiException e) {
			throw new AuthenticationException(e.getMessage());
		}
	}

}
