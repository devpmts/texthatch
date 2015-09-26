package com.devpmts.dropaline.parts;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import com.devpmts.dropaline.application.THJavaApplication;

@Singleton
@Creatable
public class LineInputPart {

	@Inject
	public LineInputPart() {
		System.err.println("create lineinputpart");
	}

	@PostConstruct
	public void postConstruct(Composite parent) {
		parent.setLayout(new FillLayout());
		new LineInputComposite(parent, SWT.FILL);
		THJavaApplication.main(new String[0]);
	}

	@Focus
	public void onFocus() {
	}

	@Persist
	public void save() {

	}

}