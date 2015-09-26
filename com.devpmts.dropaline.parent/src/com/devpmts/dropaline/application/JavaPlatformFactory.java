package com.devpmts.dropaline.application;

import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.io.JavaFileAndDataAccess;
import com.devpmts.dropaline.ui.JavaConsoleUserInterface;
import com.devpmts.dropaline.ui.JavaGuiUserInterface;
import com.devpmts.dropaline.ui.UserInterface;

public class JavaPlatformFactory implements PlatformFactory {

	@Override
	public FileAndDataAccess createFileAndDataAccess() {
		return new JavaFileAndDataAccess();
	}

	@Override
	public UserInterface createGuiUserInterface() {
		return new JavaGuiUserInterface();
	}

	@Override
	public UserInterface createConsoleUserInterface() {
		return new JavaConsoleUserInterface();
	}

	@Override
	public void registerPimApis() {
	}

	@Override
	public PimApi getDefaultPimApi() {
		return PimApi.getAvailableApis().findFirst().get();
	}
}
