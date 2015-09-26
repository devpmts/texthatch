package com.devpmts.dropaline.application;

import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.ui.UserInterface;

public interface PlatformFactory {

	FileAndDataAccess createFileAndDataAccess();

	UserInterface createGuiUserInterface();

	UserInterface createConsoleUserInterface();

	void registerPimApis();

	PimApi getDefaultPimApi();

}