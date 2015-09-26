package com.devpmts.dropaline.application;

import java.util.Arrays;
import java.util.LinkedList;

import com.devpmts.DevsLogger;

public class ArgsProcessor {

	private static final String[] TEST_ARGS = new String[] { "--send", "test",
			"not", "existing", "--learnmode", "false" };

	private LinkedList<ThArgument> arguments = new LinkedList<>();

	private final String[] args;

	public ArgsProcessor(String[] args) {
		this.args = TEST_ARGS;
	}

	public void readArguments() {
		Arrays.stream(args).forEach(arg -> {
			processArgumentToken(arg);
		});
		DevsLogger.log(arguments);
	}

	private void processArgumentToken(String token) {
		if (token.startsWith("--")) {
			createNewArgument(token.substring(2));
		} else {
			appendArgumentContent(token);
		}
	}

	private void createNewArgument(String token) {
		ThCommandOrOption commandOrOption = getCommandOrOption(token);
		arguments.add(new ThArgument(commandOrOption));
	}

	private ThCommandOrOption getCommandOrOption(String command) {
		if (!Arrays.toString(ThCommandOrOption.values()).contains(command)) {
			handleWrongArgumentInput(command);
		}
		return ThCommandOrOption.valueOf(command.toUpperCase());
	}

	private void handleWrongArgumentInput(String command) {
		ThError.commandNotRecognizedError(command);
		ThApplication.exitApplication();
	}

	private void appendArgumentContent(String argumentPart) {
		arguments.getLast().appendContent(argumentPart);
	}

	public void processCommandLineArguments() {
		arguments.stream().forEach(argument -> {
			argument.process();
		});
	}

}
