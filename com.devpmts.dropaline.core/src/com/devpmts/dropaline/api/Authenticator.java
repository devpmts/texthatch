package com.devpmts.dropaline.api;

import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.delivery.CacheDeliveryService;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.StringUtil;
import com.devpmts.util.e4.DI;

@Singleton
@Creatable
public abstract class Authenticator {

	@Inject
	@Optional
	protected UserInterface userInterface;

	@Inject
	@Optional
	private static FileAndDataAccess fileSystemAccess;

	@Inject
	@Optional
	@Preference(nodePath = CORE.NODE_PATH, value = CORE.USERNAME)
	String user;

	@Inject
	@Optional
	@Preference(nodePath = CORE.NODE_PATH, value = CORE.PASSWORD)
	String password;

	@Inject
	public Authenticator() {
	}

	public static void attemptAuthenticationOfPendingAccounts() {
		if (!DI.get(FileAndDataAccess.class).hasOnlineAccess()) {
			return;
		}
		DevsLogger.log(Authenticator.class, "Starting authentication process...");
		getPendingAuthenticators().forEach(authenticator -> authenticator.attemptAuthentication());
	}

	private static Stream<Authenticator> getPendingAuthenticators() {
		return PimApi.getAvailableApis()//
				.map(pimApi -> pimApi.authenticator())//
				.filter(authenticator -> !authenticator.isAuthenticated());
	}

	protected boolean authenticated;

	protected PimApi pimApi;

	public void attemptAuthentication() {
		if (StringUtil.isEmpty(userName())) {
			DevsLogger.log("no userid specified. cancelling authentication attempt");
			return;
		}
		try {
			assert !authenticated;
			authenticate();
			authenticated = true;
		} catch (AuthenticationException a) {
			handleAuthenticationException(a);
		}
		finalizeAuthentication();
	}

	public abstract void authenticate() throws AuthenticationException;

	private void finalizeAuthentication() {
		if (!authenticated) {
			return;
		}
		DevsLogger.log("# successfully authenticated " + pimApi.name());
		CacheDeliveryService.trigger();
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public String password() {
		return password;
	}

	public PimApi pimApi() {
		return pimApi;
	}

	public void setCredentials(String email, String password) throws AuthenticationException {
		DI.setPreference(CORE.USERNAME, email);
		DI.setPreference(CORE.PASSWORD, password);
	}

	void setPimApi(PimApi api) {
		this.pimApi = api;
	}

	public String userName() {
		return user;
	}

	public void authenticate(String email, String password) {
		try {
			setCredentials(email, password);
		} catch (AuthenticationException e) {
			handleAuthenticationException(e);
		}
		attemptAuthentication();
	}

	private void handleAuthenticationException(AuthenticationException e) {
		authenticated = false;
		DevsLogger.log(this, e);
		userInterface.handleAuthenticationFailed(pimApi(), e);
	}

}
