package com.devpmts.dropaline.gui;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.eclipse.e4.ui.model.application.MApplication;

import com.devpmts.DevsLogger;
import com.devpmts.util.e4.DI;

public class TrayManager {

	static long time;

	public static TrayIcon trayIcon;

	public void installTrayIcon() {
		trayIcon = null;
		if (SystemTray.isSupported()) {
			SystemTray tray = SystemTray.getSystemTray();
			Image image = null;
			try {
				image = ImageIO.read(getClass().getResourceAsStream("/BlueDonut.gif"));
			} catch (IOException e1) {
				DevsLogger.log(e1);
			}
			trayIcon = new TrayIcon(image, "toToodle");

			trayIcon.addMouseListener(new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					System.out.print(" .clicked ");
					if (e.getButton() == MouseEvent.BUTTON3) {
						System.exit(0);
					}
					// if (frame.getBounds().height == 0) {
					DevsLogger.log("trayOpen");
					DevsLogger.log("tray clicked");
					showHide();
					// } else {
					// TextHatchLogger.log("should close...");
					// }
				}
			});
			try {
				tray.add(trayIcon);
			} catch (AWTException e) {
				System.err.println(e);
			}
		}
	}

	public void showHide() {
		if (isAppVisible()) {
			close();
		} else {
			open();
		}

	}

	public void close() {
		if (System.currentTimeMillis() - TrayManager.time > 100) {
			if (isAppVisible()) {
				TrayManager.time = System.currentTimeMillis();
			}
		}
	}

	public void open() {
		if (System.currentTimeMillis() - TrayManager.time > 100) {
			if (!isAppVisible()) {
				TrayManager.time = System.currentTimeMillis();
			}
		}
	}

	private boolean isAppVisible() {
		return DI.get(MApplication.class).isVisible();
	}

	public void uninstallTrayIcon() {
		SystemTray.getSystemTray().remove(trayIcon);
	}
}
