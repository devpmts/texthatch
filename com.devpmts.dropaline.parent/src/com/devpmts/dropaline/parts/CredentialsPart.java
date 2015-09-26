package com.devpmts.dropaline.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.api.Authenticator;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.gui.DROPALINE_GUI;
import com.devpmts.util.e4.DI;
import com.devpmts.util.e4.UI;

public class CredentialsPart {

	private Button cancel;

	@Inject
	private MDirtyable dirty;

	public Text email;

	private Label emailLabel;

	public Text password;

	private Label passwordLabel;

	private Button login;

	public Label warning;

	@Inject
	private MWindow window;

	Authenticator authenticator;

	private FormData fd_login;

	@Inject
	public CredentialsPart() {
		System.err.println("creating credentialsPart");
	}

	@PostConstruct
	public void createComposite(Composite parent) {
		parent.setLayout(new FormLayout());
		setupSaveButton(parent);
		setupCancel(parent);

		emailLabel = new Label(parent, SWT.LEFT);
		FormData fd_emailLabel = new FormData();
		fd_emailLabel.left = new FormAttachment(0, 5);
		emailLabel.setLayoutData(fd_emailLabel);

		emailLabel.setText("Email: ");
		email = new Text(parent, SWT.SINGLE);
		email.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
				applyEnterKey(e);
			}
		});
		fd_emailLabel.top = new FormAttachment(email, 0, SWT.TOP);
		FormData fd_email = new FormData();
		fd_email.left = new FormAttachment(0, 74);
		fd_email.right = new FormAttachment(100, -10);
		fd_email.top = new FormAttachment(0, 10);
		email.setLayoutData(fd_email);

		passwordLabel = new Label(parent, SWT.LEFT);
		FormData fd_passwordLabel = new FormData();
		fd_passwordLabel.left = new FormAttachment(emailLabel, 0, SWT.LEFT);
		passwordLabel.setLayoutData(fd_passwordLabel);
		passwordLabel.setText("Password: ");
		password = new Text(parent, SWT.PASSWORD);
		password.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(org.eclipse.swt.events.KeyEvent e) {
				applyEnterKey(e);
			}
		});
		fd_passwordLabel.bottom = new FormAttachment(password, 0, SWT.BOTTOM);
		FormData fd_password = new FormData();
		fd_password.top = new FormAttachment(email, 6);
		fd_password.left = new FormAttachment(email, 0, SWT.LEFT);
		fd_password.right = new FormAttachment(100, -10);
		password.setLayoutData(fd_password);

		warning = new Label(parent, SWT.LEFT);
		warning.setText("warning");
		FormData fd_warning = new FormData();
		fd_warning.top = new FormAttachment(passwordLabel, 21);
		fd_warning.bottom = new FormAttachment(cancel, -6);
		fd_warning.left = new FormAttachment(emailLabel, 0, SWT.LEFT);
		fd_warning.right = new FormAttachment(100, -10);
		warning.setLayoutData(fd_warning);
		warning.setForeground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

		DI.set(DROPALINE_GUI.CREDENTIALS_WINDOW_ID, window);
		DI.set(CredentialsPart.class, this);
	}

	private void setupCancel(Composite parent) {
		cancel = new Button(parent, SWT.PUSH);
		cancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				closeWindow();
			}
		});
		fd_login.bottom = new FormAttachment(cancel, 0, SWT.BOTTOM);
		fd_login.top = new FormAttachment(cancel, 0, SWT.TOP);
		fd_login.right = new FormAttachment(cancel, -6);
		FormData fd_cancel = new FormData();
		fd_cancel.left = new FormAttachment(0, 221);
		cancel.setLayoutData(fd_cancel);
		fd_cancel.top = new FormAttachment(0, 106);
		cancel.setText("cancel");
	}

	private void setupSaveButton(Composite parent) {
		login = new Button(parent, SWT.PUSH);
		login.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				storeCredentialsAndAuthenticate();
			}
		});
		fd_login = new FormData();
		fd_login.left = new FormAttachment(0, 146);
		login.setLayoutData(fd_login);
		login.setText("login");
	}

	@Persist
	public void save() {
		dirty.setDirty(false);
	}

	private void applyEnterKey(KeyEvent evt) {
		if (evt.keyCode == SWT.CR) {
			storeCredentialsAndAuthenticate();
		}
	}

	void storeCredentialsAndAuthenticate() {
		authenticator.authenticate(email.getText(), password.getText());
		if (authenticator.isAuthenticated())
			closeWindow();
	}

	private void closeWindow() {
		UI.closeWindow(DROPALINE_GUI.CREDENTIALS_WINDOW_ID);
	}

	public void setAuthenticator(Authenticator authenticator) {
		this.authenticator = authenticator;
		try {
			if (authenticator.userName() == null) {
				return;
			}
			email.setText(authenticator.userName());
			password.setText(authenticator.password());
		} catch (Exception e) {
			DevsLogger.log(e);
		}
		warning.setVisible(true);
		warning.setText(authenticator.pimApi().name());
	}

	public static void open(PimApi api) {
		UI.openWindow(DROPALINE_GUI.CREDENTIALS_WINDOW_ID);
		Display.getDefault().syncExec(() -> DI.get(CredentialsPart.class).init(api));
	}

	private void init(PimApi api) {
		DevsLogger.log("setCredentialsView " + api);
		setAuthenticator(api.authenticator());
		email.setFocus();
	}
}