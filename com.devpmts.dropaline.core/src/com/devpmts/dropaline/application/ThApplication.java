package com.devpmts.dropaline.application;

import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.e4.DI;

public abstract class ThApplication {

	protected ArgsProcessor argsProcessor;

	public ThApplication() {
		DI.injectContextIn(this);
	}

	public void boot(String[] args) {
		createPlatformSpecificObjects(createPlatformFactory());
		readProgramArguments(args);
		start();
	}

	void createPlatformSpecificObjects(PlatformFactory factory) {
		if ((boolean) DI.get(CORE.CONSOLE_TO_FILE)) {
			DevsLogger.directConsoleOutputToFile(CORE.LOG_OUTPUT_FILE);
		}
		DI.set(FileAndDataAccess.class, factory.createFileAndDataAccess());
		if ((Boolean) DI.get(CORE.RUNNING_GUI))
			DI.set(UserInterface.class, factory.createGuiUserInterface());
		else
			DI.set(UserInterface.class, factory.createConsoleUserInterface());
		DI.injectContextIn(DI.get(FileAndDataAccess.class));
		factory.registerPimApis();
		DI.set(CORE.DEFAULT_API, factory.getDefaultPimApi());
	}

	private void start() {
		startApplication();
		// Authenticator.attemptAuthenticationOfPendingAccounts();
	}

	@Inject
	private void registerPlugins(IExtensionRegistry extensionRegistry) throws CoreException {
		IConfigurationElement[] configElements = extensionRegistry
				.getConfigurationElementsFor(CORE.PLUGIN_EXTENSION_ID);
		for (IConfigurationElement element : configElements) {
			element.createExecutableExtension("class");
		}
	}

	private void readProgramArguments(String[] args) {
		argsProcessor = new ArgsProcessor(args);
		argsProcessor.readArguments();
	}

	protected abstract PlatformFactory createPlatformFactory();

	public abstract void startApplication();

	public static void exitApplication() {
		System.exit(0);
	}

}