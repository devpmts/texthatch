package com.devpmts.dropaline.application;

import javax.inject.Inject;
import javax.inject.Named;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.util.e4.DI;

public class ThArgument {

	@Inject
	@Named(CORE.DEFAULT_API)
	private PimApi defaultApi;

	private ThCommandOrOption command;

	private String content = "";

	public ThArgument(ThCommandOrOption command) {
		this.command = command;
	}

	void appendContent(String append) {
		this.content = getContent() + " " + append;
	}

	@Override
	public String toString() {
		return "--" + getCommand() + " " + getContent();
	}

	public String getContent() {
		return content;
	}

	public ThCommandOrOption getCommand() {
		return command;
	}

	public void process() {
		switch (command) {
		case GUI:
			break;
		case SEND:
			defaultApi.parse(new LineAndNote(content, ""));
			break;
		case LEARNMODE:
			assertFlag();
			DI.set(ThCommandOrOption.LEARNMODE.toString(), Boolean.TRUE);
			break;
		case QUICKADD:

			break;
		default:

		}
	}

	private void assertFlag() {
		assert content.isEmpty();
	}
}