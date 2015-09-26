package com.devpmts.dropaline.parts;

import java.awt.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.swt.widgets.Composite;

import com.devpmts.dropaline.gui.Settings;
import com.devpmts.util.e4.DefaultSamplePart;

public class SettingsPart extends DefaultSamplePart {

	@Inject
	Settings settings;

	@Override
	@PostConstruct
	public void createComposite(Composite parent) {
		super.createComposite(parent);
	}

	@Override
	protected Component createPanel(Composite parent) {
		return settings;
	}

}