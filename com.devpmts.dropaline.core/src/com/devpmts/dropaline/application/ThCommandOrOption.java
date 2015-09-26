package com.devpmts.dropaline.application;

public enum ThCommandOrOption {
	GUI("gui"), SEND("send"), LEARNMODE("learnmode"), QUICKADD("quickadd");

	private String command;

	private ThCommandOrOption(String command) {
		this.command = command;
	}

	@Override
	public String toString() {
		return command;
	}
}