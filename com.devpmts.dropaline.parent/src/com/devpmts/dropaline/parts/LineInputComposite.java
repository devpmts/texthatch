package com.devpmts.dropaline.parts;

import java.awt.event.KeyEvent;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.gui.DROPALINE_GUI;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.util.e4.DI;
import com.devpmts.util.e4.UI;

@Singleton
@Creatable
public class LineInputComposite extends Composite {

	private static Text lineInput;

	private Button openSettingsButton;

	public Button noteToggle;

	@Inject
	@Named(CORE.DEFAULT_API)
	@Optional
	private PimApi defaultApi;

	@Inject
	@Named(DROPALINE_GUI.NOTE_INPUT)
	@Optional
	private Text noteInput;

	@Inject
	@Optional
	private NoteInputPart noteInputPart;

	@Inject
	MApplication app;

	@Inject
	@Optional
	MWindow window;

	public static final int LINE_HEIGHT = 45;

	@Inject
	public LineInputComposite(Composite parent, int style) {
		super(parent, SWT.NONE);

		setupLayout();
		setupNoteToggle();
		setupLineInput();
		setupOpenSettings();

		DI.set(DROPALINE_GUI.LINE_INPUT, lineInput);
		DI.set(DROPALINE_GUI.ACTIVE_FIELD, lineInput);
		DI.injectContextIn(this);
	}

	private void setupLayout() {
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.horizontalSpacing = 0;
		gridLayout.verticalSpacing = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		((GridLayout) getLayout()).marginWidth = 0;
	}

	private void setupOpenSettings() {
		openSettingsButton = new Button(this, SWT.RIGHT);
		openSettingsButton.setAlignment(SWT.CENTER);
		GridData gd_openSettingsButton = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_openSettingsButton.widthHint = 20;
		gd_openSettingsButton.heightHint = 20;
		openSettingsButton.setLayoutData(gd_openSettingsButton);
		openSettingsButton.setFont(SWTResourceManager.getFont("Maven Pro", 8, SWT.NORMAL));
		openSettingsButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				openSettingsDialog();
			}

		});
	}

	private void setupLineInput() {
		lineInput = new Text(this, SWT.BORDER);
		GridData gd_lineInput = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 2);
		gd_lineInput.heightHint = 40;
		gd_lineInput.widthHint = 450;
		lineInput.setLayoutData(gd_lineInput);
		lineInput.setFont(SWTResourceManager.getFont("Maven Pro", 16, SWT.NORMAL));
		lineInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(org.eclipse.swt.events.KeyEvent evt) {
				lineInputKeyEvent(evt);
			}
		});
	}

	private void setupNoteToggle() {
		noteToggle = new Button(this, SWT.TOGGLE);
		GridData gd_noteToggle = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_noteToggle.heightHint = 20;
		gd_noteToggle.widthHint = 20;
		noteToggle.setLayoutData(gd_noteToggle);
		noteToggle.setImage(ResourceManager.getPluginImage("com.devpmts.dropaline", "icons/sample.png"));
		noteToggle.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (noteToggle.getSelection()) {
					expandNotes();
				} else {
					collapseNotes();
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	void lineInputKeyEvent(org.eclipse.swt.events.KeyEvent evt) {
		switch (evt.keyCode) {
		case SWT.CR: {
			defaultApi.parse(new LineAndNote(lineInput.getText(), noteInput.getText()));
			break;
		}
		case KeyEvent.VK_DOWN: {
			expandNotes();
			break;
		}
		case KeyEvent.VK_UP: {
			collapseNotes();
			break;
		}
		case KeyEvent.VK_O: {
			if (evt.stateMask == 1) {
				openSettingsDialog();
			}
			break;
		}
		}

	}

	private void openSettingsDialog() {
		UI.openWindow(DROPALINE_GUI.SETTINGS_WINDOW_ID);
	}

	private void expandNotes() {
		noteInputPart.expand();
		noteToggle.setSelection(true);
	}

	public void collapseNotes() {
		noteInputPart.collapse();
		noteToggle.setSelection(false);
	}

	public static void setText(String content) {
		lineInput.setText(content);
	}

	public static Text getLineInput() {
		return lineInput;
	}

}
