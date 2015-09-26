package com.devpmts.dropaline.application;

import com.devpmts.dropaline.CORE;
import com.devpmts.util.e4.DI;

public class THJavaApplication extends ThApplication {

	public static void main(String[] args) {
		new THJavaApplication().boot(args);
	}

	@Override
	protected PlatformFactory createPlatformFactory() {
		return new JavaPlatformFactory();
	}

	@Override
	public void startApplication() {
		if (!(boolean) DI.get(CORE.RUNNING_GUI))
			argsProcessor.processCommandLineArguments();
	}

}
