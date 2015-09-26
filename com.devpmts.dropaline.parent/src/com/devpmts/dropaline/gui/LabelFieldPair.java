package com.devpmts.dropaline.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.devpmts.util.e4.DI;

public class LabelFieldPair extends JPanel {

	Field field;

	String value;

	String fieldName;

	private JTextField jTextField2;

	public JLabel name;

	public String defaultString;

	public LabelFieldPair(Field field, int cut) {
		this.field = field;
		defaultString = getValue();
		if ((value = DI.getString(field.getName())) == null) {
			value = getValue();
		}
		fieldName = field.getName().substring(0, field.getName().length() - cut);
		// TextHatchLogger.log(field.getName());
		// String[] tmp = field.getName().split(".");
		// fieldName = tmp[tmp.length-1];
		jTextField2 = new JTextField();
		name = new JLabel();

		setMinimumSize(new Dimension(130, 40));
		setPreferredSize(new Dimension(130, 40));
		// setLayout(new AbsoluteLayout());

		jTextField2.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		jTextField2.setHorizontalAlignment(JTextField.CENTER);
		jTextField2.setText(value);
		jTextField2.setPreferredSize(new Dimension(40, 21));
		add(jTextField2);

		name.setFont(new Font("Tahoma", 0, 12)); // NOI18N
		name.setForeground(new Color(0, 153, 0));
		name.setText(fieldName + ":");
		add(name);
	}

	void apply() {
		try {
			field.set(null, jTextField2.getText());
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(LabelFieldPair.class.getName()).log(Level.SEVERE, null, ex);
		}
		DI.set(field.getName(), jTextField2.getText());
	}

	void reset() {
		jTextField2.setText(defaultString);
		DI.set(field.getName(), jTextField2.getText());
	}

	private String getValue() {
		try {
			return (String) field.get(null);
		} catch (IllegalArgumentException | IllegalAccessException ex) {
			Logger.getLogger(LabelFieldPair.class.getName()).log(Level.SEVERE, null, ex);
		}
		return null;
	}
}
