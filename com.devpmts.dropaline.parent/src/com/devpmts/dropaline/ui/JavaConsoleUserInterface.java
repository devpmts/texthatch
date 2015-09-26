package com.devpmts.dropaline.ui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.api.AuthenticationException;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.util.StringUtil;

public class JavaConsoleUserInterface implements UserInterface {

	@Override
	public void requestCredentialsFromUser(PimApi pimApi) {
		if (!pimApi.authenticator().isAuthenticated()) {
			presentMessageToUser("Enter credentials", "Please type your task providers username and password");
			String username = readLine();
			String password = readLine();
			try {
				pimApi.authenticator().setCredentials(username, password);
			} catch (AuthenticationException e) {
				handleAuthenticationFailed(pimApi, e);
			}
		}
		pimApi.authenticator().attemptAuthentication();
	}

	@Override
	public void handleSuccessfulDelivery(List<PimItem> items) {
		consoleOut("The following items were sent successfully:");
		items.forEach(pimItem -> consoleOut(pimItem.getHumanReadableIdentifier()));
		consoleOut();
	}

	@Override
	public void handleAuthenticationFailed(PimApi api, AuthenticationException ae) {
		consoleOut("Authentication error. Do you want to try again? (y/n)");
		if (readLine().equalsIgnoreCase("y")) {
			requestCredentialsFromUser(api);
		}
	}

	private String readLine() {
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			return reader.readLine();
		} catch (IOException e) {
			DevsLogger.log("no console input");
			return "";
		}
	}

	@Override
	public void presentMessageToUser(String title, String message) {
		if (!StringUtil.isEmpty(title)) {
			consoleOut(title);
			underline(title);
		}
		consoleOut(message);
	}

	@Override
	public void preparePimItemTransfer(PimItem item) {
	}

	private void underline(String line) {
		char[] titleUnderLine = new char[line.length()];
		Arrays.fill(titleUnderLine, '#');
		consoleOut(new String(titleUnderLine));
	}

	private void consoleOut(String... lines) {
		Arrays.stream(lines).forEach(line -> System.out.println(line));
	}

	@Override
	public String getTextLine() {
		assert false;
		return null;
	}

}
