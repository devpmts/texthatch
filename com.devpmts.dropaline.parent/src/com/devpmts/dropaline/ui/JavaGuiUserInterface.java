package com.devpmts.dropaline.ui;

import java.awt.TrayIcon.MessageType;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JOptionPane;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Text;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.api.AuthenticationException;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.gui.DROPALINE_GUI;
import com.devpmts.dropaline.gui.TrayManager;
import com.devpmts.dropaline.parts.CredentialsPart;
import com.devpmts.dropaline.parts.LineInputComposite;
import com.devpmts.dropaline.parts.NoteInputPart;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.util.e4.DI;

@Singleton
@Creatable
public class JavaGuiUserInterface implements UserInterface {

	@Inject
	@Optional
	private NoteInputPart noteInputPart;

	@Inject
	@Named(DROPALINE_GUI.LINE_INPUT)
	@Optional
	private Text lineInput;

	@Inject
	@Named(DROPALINE_GUI.NOTE_INPUT)
	@Optional
	private Text noteInput;

	@Inject
	public JavaGuiUserInterface() {
		DI.injectContextIn(this);
	}

	@Override
	public void handleAuthenticationFailed(PimApi api, AuthenticationException ae) {
		DevsLogger.log("handling LOGIN FAILED... " + "(" + api.name() + ")");
		// CredentialsPart.open(api);
	}

	@Override
	public void handleNotFoundInList(String wasNotFound, Object[] list, String listItemType) {
		String message = UserInterface.createNotFoundMessage(list, listItemType);
		noteInputPart.collapse();
		String title = listItemType + " " + wasNotFound + " doesn't exist";
		JOptionPane.showInternalMessageDialog(null, message, title, 1);
	}

	@Override
	public void presentMessageToUser(String itemString, String messageTitle) {
		TrayManager.trayIcon.displayMessage(messageTitle, itemString, MessageType.NONE);
	}

	@Override
	public void preparePimItemTransfer(PimItem item) {
		LineInputComposite.setText(item.getTitle());
		noteInput.setText(item.getNote());
	}

	@Override
	public void handleSuccessfulDelivery(List<PimItem> items) {
		lineInput.setText("");
		noteInput.setText("");
		noteInputPart.collapse();
	}

	@Override
	public void requestCredentialsFromUser(PimApi pimApi) {
		CredentialsPart.open(pimApi);
	}

	@Override
	public String getTextLine() {
		return lineInput.getText();
	}

}
