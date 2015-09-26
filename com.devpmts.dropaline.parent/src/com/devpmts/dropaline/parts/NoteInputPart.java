package com.devpmts.dropaline.parts;

import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.gui.DROPALINE_GUI;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.util.e4.DI;

@Singleton
public class NoteInputPart {

	private static final int NOTE_HEIGHT = 230;

	private Text noteInput;

	@Inject
	@Optional
	private LineInputComposite lineInputComposite;

	@Inject
	@Optional
	private MWindow window;

	@Inject
	@Optional
	private MPart part;

	@Inject
	@Optional
	@Named(DROPALINE_GUI.LINE_INPUT)
	private Text lineInput;

	@Inject
	@Optional
	@Named(CORE.DEFAULT_API)
	private PimApi defaultApi;

	@Inject
	public NoteInputPart() {
		System.err.println("create NotePart");
		DI.set(NoteInputPart.class, this);
		DI.injectContextIn(this);
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		noteInput = new Text(parent, SWT.MULTI);
		noteInput.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(org.eclipse.swt.events.KeyEvent evt) {
				noteFieldKeys(evt);
			}
		});
		collapse();
		DI.set(DROPALINE_GUI.NOTE_INPUT, noteInput);
	}

	void noteFieldKeys(org.eclipse.swt.events.KeyEvent evt) {
		switch (evt.keyCode) {
		case KeyEvent.VK_O: {
			// TODO
			if (evt.stateMask == 1) {
				assert false;
			}
			break;
		}
		case KeyEvent.VK_ENTER: {
			if (evt.stateMask == 2) {
				defaultApi.parse(new LineAndNote(lineInput.getText(), noteInput.getText()));
			}
			break;
		}
		case KeyEvent.VK_UP: {
			try {
				if (noteInput.getCaretPosition() == 0) {
					lineInputComposite.collapseNotes();
					return;
				}
				int maxChar = new BufferedReader(new StringReader(noteInput.getText())).readLine().length();
				int noteCaret = noteInput.getCaretPosition();
				if (noteCaret <= maxChar) {
					// int taskLength = DROPALINE.lineInput.getText().length();
					// int taskCaret = (taskLength > noteCaret) ? noteCaret :
					// taskLength;
					lineInput.setFocus();
					// input.getTaskField().setCaretPosition(taskCaret);
				}
			} catch (IOException ex) {
				Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
			}
			break;
		}
		}
	}

	@Focus
	public void onFocus() {

	}

	@Persist
	public void save() {

	}

	public void expand() {
		part.setVisible(true);
		noteInput.setFocus();
		window.setHeight(50 + LineInputComposite.LINE_HEIGHT + NOTE_HEIGHT);
		DI.set(DROPALINE_GUI.ACTIVE_FIELD, noteInput);
	}

	public void collapse() {
		part.setVisible(false);
		window.setHeight(50 + LineInputComposite.LINE_HEIGHT);
		DI.set(DROPALINE_GUI.ACTIVE_FIELD, lineInput);
	}

	public void setText(String content) {
		noteInput.setText(content);
	}
}