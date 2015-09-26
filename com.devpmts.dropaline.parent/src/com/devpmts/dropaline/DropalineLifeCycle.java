package com.devpmts.dropaline;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;

import com.devpmts.dropaline.gui.TrayManager;
import com.devpmts.util.e4.DI;

public class DropalineLifeCycle {

	@Inject
	IEclipseContext context;

	TrayManager trayManager = new TrayManager();

	@PostContextCreate
	void context() {
		DI.context.set(context);
		DI.preferenceNode.set(CORE.NODE_PATH);
		DI.set(CORE.CONSOLE_TO_FILE, false);
		DI.set(CORE.RUNNING_GUI, true);
		DI.set(CORE.LEARNING_MODE, false);
		trayManager.installTrayIcon();
	}

	@PreSave
	void close() {
		trayManager.uninstallTrayIcon();
	}
}
