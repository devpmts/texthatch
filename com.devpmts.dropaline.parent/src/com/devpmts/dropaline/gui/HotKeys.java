//package com.devpmts.dropaline.gui;
//
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.KeyAdapter;
//import java.awt.event.KeyEvent;
//
//import javax.swing.ButtonGroup;
//import javax.swing.GroupLayout;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.LayoutStyle;
//
//import com.devpmts.util.e4.DI;
//import com.melloware.jintellitype.JIntellitype;
//
//public class HotKeys extends JPanel {
//
//	public static int key = 72;
//
//	public static int modifier = JIntellitype.MOD_WIN;
//
//	private JCheckBox alt;
//
//	private ButtonGroup buttonGroup1;
//
//	private JCheckBox control;
//
//	private JButton jButton1;
//
//	private JLabel keyLabel;
//
//	private JLabel modifierLabel;
//
//	private JCheckBox none;
//
//	private JCheckBox shift;
//
//	private JTextField showChar;
//
//	private JCheckBox windows;
//
//	public HotKeys() {
//		buttonGroup1 = new ButtonGroup();
//		showChar = new JTextField();
//		modifierLabel = new JLabel();
//		keyLabel = new JLabel();
//		windows = new JCheckBox();
//		alt = new JCheckBox();
//		control = new JCheckBox();
//		shift = new JCheckBox();
//		jButton1 = new JButton();
//		none = new JCheckBox();
//
//		showChar.setEditable(false);
//		showChar.setHorizontalAlignment(JTextField.CENTER);
//		showChar.addKeyListener(new KeyAdapter() {
//
//			@Override
//			public void keyPressed(KeyEvent evt) {
//				showChar.setText(Character.toString(evt.getKeyChar()));
//				key = evt.getKeyCode();
//			}
//		});
//
//		modifierLabel.setText("Modifier:");
//
//		keyLabel.setText("Key:");
//
//		buttonGroup1.add(windows);
//		windows.setSelected(true);
//		windows.setText("Windows");
//		windows.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				modifier = JIntellitype.MOD_WIN;
//			}
//		});
//
//		buttonGroup1.add(alt);
//		alt.setText("Alt");
//		alt.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				modifier = JIntellitype.MOD_ALT;
//			}
//		});
//
//		buttonGroup1.add(control);
//		control.setText("Control");
//		control.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				modifier = JIntellitype.MOD_CONTROL;
//			}
//		});
//
//		buttonGroup1.add(shift);
//		shift.setText("Shift");
//		shift.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				modifier = JIntellitype.MOD_SHIFT;
//			}
//		});
//
//		jButton1.setText("apply");
//		jButton1.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				DI.set("modifier", modifier);
//				DI.set("key", key);
//				// MainDoodle.input.installHotkeys();
//			}
//		});
//
//		buttonGroup1.add(none);
//		none.setText("none");
//		none.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				modifier = 0;
//			}
//		});
//
//		GroupLayout layout = new GroupLayout(this);
//		this.setLayout(layout);
//		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//				.addGroup(
//						layout.createSequentialGroup()
//								.addContainerGap()
//								.addGroup(
//										layout.createParallelGroup(GroupLayout.Alignment.LEADING)
//												.addGroup(
//														layout.createSequentialGroup()
//																.addGroup(
//																		layout.createParallelGroup(
//																				GroupLayout.Alignment.LEADING)
//																				.addComponent(modifierLabel)
//																				.addComponent(windows)
//																				.addComponent(alt))
//																.addPreferredGap(
//																		LayoutStyle.ComponentPlacement.UNRELATED)
//																.addGroup(
//																		layout.createParallelGroup(
//																				GroupLayout.Alignment.LEADING)
//																				.addComponent(shift)
//																				.addComponent(control))
//																.addPreferredGap(
//																		LayoutStyle.ComponentPlacement.UNRELATED)
//																.addGroup(
//																		layout.createParallelGroup(
//																				GroupLayout.Alignment.LEADING)
//																				.addComponent(showChar,
//																						GroupLayout.PREFERRED_SIZE, 36,
//																						GroupLayout.PREFERRED_SIZE)
//																				.addComponent(keyLabel)))
//												.addGroup(
//														layout.createSequentialGroup().addComponent(none)
//																.addGap(135, 135, 135).addComponent(jButton1)))
//								.addContainerGap(149, Short.MAX_VALUE)));
//		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addGroup(
//				layout.createSequentialGroup()
//						.addGap(16, 16, 16)
//						.addGroup(
//								layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
//										.addGroup(
//												layout.createSequentialGroup()
//														.addComponent(modifierLabel)
//														.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//														.addGroup(
//																layout.createParallelGroup(
//																		GroupLayout.Alignment.BASELINE)
//																		.addComponent(windows)
//																		.addComponent(control)
//																		.addComponent(showChar,
//																				GroupLayout.PREFERRED_SIZE,
//																				GroupLayout.DEFAULT_SIZE,
//																				GroupLayout.PREFERRED_SIZE)))
//										.addGroup(
//												layout.createSequentialGroup().addComponent(keyLabel)
//														.addGap(29, 29, 29)))
//						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
//						.addGroup(
//								layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(alt)
//										.addComponent(shift))
//						.addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
//						.addGroup(
//								layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(none)
//										.addComponent(jButton1)).addContainerGap(188, Short.MAX_VALUE)));
//		switch (DI.get("modifier", 0)) {
//		case 0:
//			none.setSelected(true);
//			break;
//		case JIntellitype.MOD_WIN:
//			windows.setSelected(true);
//			break;
//		case JIntellitype.MOD_ALT:
//			alt.setSelected(true);
//			break;
//		case JIntellitype.MOD_SHIFT:
//			shift.setSelected(true);
//			break;
//		case JIntellitype.MOD_CONTROL:
//			control.setSelected(true);
//			break;
//		}
//		Character i = DI.get("key", (char) 92);
//		showChar.setText(Character.toString(i));
//	}
//
// }
