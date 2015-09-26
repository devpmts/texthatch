package com.devpmts.dropaline.application;

import javax.inject.Inject;

import com.devpmts.dropaline.ui.UserInterface;

public enum ThError {
	COMMAND_NOT_RECOGNIZED, ITEM_NOT_FOUND_IN_LIST;

	@Inject
	private static UserInterface userInterface;

	public static void error(ThError error, String message) {
		userInterface.showErrorMessage(error, message);
	}

	public static void commandNotRecognizedError(String command) {
		error(ThError.COMMAND_NOT_RECOGNIZED, "Command " + command + " not recognized.");
	}

}
