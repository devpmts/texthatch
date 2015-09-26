package com.devpmts.dropaline.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.osgi.service.prefs.BackingStoreException;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.application.ThApplication;
import com.devpmts.dropaline.parts.CredentialsPart;
import com.devpmts.util.e4.DI;

@Singleton
@Creatable
public class Settings extends JTabbedPane {

	private JPanel customize;

	private JPanel dateTime;

	private JLabel jLabel2;

	private JLabel jLabel3;

	private JLabel jLabel4;

	private JLabel jLabel5;

	private JCheckBox learnMode;

	private JCheckBox logFile;

	private JScrollPane pluginScroll;

	private JPanel pluginSettings;

	private JPanel symbols;

	@Inject
	public Settings() {
		System.err.println("creating settings");
		initComponents();
		logFile.setSelected(DI.get("logFile", false));
		// constructSymbolFields();
		createPluginGuis();
		// createDatePatternGui();
		setUI(new BasicTabbedPaneUI() {

			@Override
			protected void paintContentBorder(Graphics g, int tabPlacement, int selectedIndex) {
				super.paintContentBorder(g, tabPlacement, selectedIndex);
			}

			@Override
			protected void paintTab(Graphics g, int tabPlacement, Rectangle[] rects, int tabIndex, Rectangle iconRect,
					Rectangle textRect) {
				super.paintTab(g, tabPlacement, rects, tabIndex, iconRect, textRect);
			}

			@Override
			protected void paintTabBackground(Graphics g, int tabPlacement, int tabIndex, int x, int y, int w, int h,
					boolean isSelected) {
			}
		});
		customize.add(createLocaleSelection());
		customize
				.add(new JLabel(
						"                                                                                                    "));
		boolean tmp = DI.get("learnMode", false);
		DI.set(CORE.LEARNING_MODE, tmp);
		learnMode.setSelected(tmp);
	}

	private void initComponents() {
		symbols = new JPanel();
		jLabel2 = new JLabel();
		customize = new JPanel();
		jLabel3 = new JLabel();
		learnMode = new JCheckBox();
		logFile = new JCheckBox();
		pluginScroll = new JScrollPane();
		pluginSettings = new JPanel();
		jLabel5 = new JLabel();
		dateTime = new JPanel();
		jLabel4 = new JLabel();
		setTabPlacement(JTabbedPane.LEFT);
		symbols.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));
		jLabel2.setFont(new Font("Tahoma", 0, 14)); // NOI18N
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setText("symbols");
		jLabel2.setMaximumSize(new Dimension(350, 25));
		jLabel2.setMinimumSize(new Dimension(350, 25));
		jLabel2.setPreferredSize(new Dimension(320, 50));
		symbols.add(jLabel2);

		addTab("symbols", symbols);

		customize.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));

		jLabel3.setFont(new Font("Tahoma", 0, 14)); // NOI18N
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setText("custom settings");
		jLabel3.setMaximumSize(new Dimension(400, 25));
		jLabel3.setMinimumSize(new Dimension(400, 25));
		jLabel3.setPreferredSize(new Dimension(320, 50));
		customize.add(jLabel3);

		learnMode.setText("Learn Mode");
		learnMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				boolean tmp = learnMode.isSelected();
				DI.set(CORE.LEARNING_MODE, tmp);
				DI.set("learnMode", tmp);
			}
		});
		customize.add(learnMode);

		logFile.setText("Log File");
		logFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				DI.set(CORE.CONSOLE_TO_FILE, logFile.isSelected());
				DI.set("logFile", logFile.isSelected());
			}
		});
		customize.add(logFile);

		addTab("customize", customize);

		pluginScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

		pluginSettings.setLayout(new BoxLayout(pluginSettings, BoxLayout.Y_AXIS));

		jLabel5.setFont(new Font("Tahoma", 0, 14)); // NOI18N
		jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel5.setText("Plugins                                         ");
		jLabel5.setAutoscrolls(true);
		jLabel5.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel5.setMaximumSize(new Dimension(350, 50));
		jLabel5.setMinimumSize(new Dimension(250, 50));
		jLabel5.setPreferredSize(new Dimension(310, 50));
		jLabel5.setRequestFocusEnabled(false);
		pluginSettings.add(jLabel5);

		pluginScroll.setViewportView(pluginSettings);

		addTab("plugins", pluginScroll);

		dateTime.setLayout(new FlowLayout(java.awt.FlowLayout.LEFT));

		jLabel4.setFont(new Font("Tahoma", 0, 14)); // NOI18N
		jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel4.setText("Date and Time Patterns");
		jLabel4.setAlignmentX(0.5F);
		jLabel4.setHorizontalTextPosition(SwingConstants.CENTER);
		jLabel4.setMaximumSize(new Dimension(400, 25));
		jLabel4.setMinimumSize(new Dimension(400, 25));
		jLabel4.setPreferredSize(new Dimension(320, 50));
		dateTime.add(jLabel4);

		addTab("date/time", dateTime);

	}

	private void apply() {
		ArrayList<LabelFieldPair> components = new ArrayList<>();
		components.addAll(getLabelFieldPairs(symbols));
		components.addAll(getLabelFieldPairs(dateTime));
		for (LabelFieldPair pair : components) {
			pair.apply();
		}
		// lineInputPart.closeSettings();
	}

	private void createPluginGuis() {
		PimApi.getAvailableApis().forEach(pimApi -> createPimApiGui(pimApi));
	}

	private void createPimApiGui(PimApi pimApi) {
		JPanel panel = new JPanel();
		JPanel filler = new JPanel();
		filler.setMaximumSize(new Dimension(1000, 15));
		filler.setPreferredSize(new Dimension(1000, 15));
		filler.setMaximumSize(new Dimension(1000, 15));
		filler.setBorder(new LineBorder(Color.gray, 8));
		panel.add(filler);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setPreferredSize(new Dimension(pluginSettings.getPreferredSize().width, 100));
		try {
			JButton button = new JButton("   Login...   ");
			button.setBorder(new LineBorder(Color.white, 3, true));
			button.setSize(150, 20);
			button.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CredentialsPart.open(pimApi);
				}
			});
			JLabel label = new JLabel(pimApi.name());
			label.setFont(new Font("Tahoma", 2, 14));
			label.setHorizontalAlignment(SwingConstants.LEFT);
			label.setMaximumSize(new Dimension(400, 25));
			label.setMinimumSize(new Dimension(400, 25));
			label.setPreferredSize(new Dimension(600, 20));
			panel.add(label);
			panel.add(button);
			final JCheckBox box = new JCheckBox(CORE.QUICKADD) {

				{
					addChangeListener(ev -> setQuickAdd(isSelected()));
					DI.injectContextIn(this);
				}

				@Inject
				@Preference(nodePath = CORE.NODE_PATH)
				IEclipsePreferences prefs;

				@Inject
				@Optional
				void setQuickAdd(@Preference(nodePath = CORE.NODE_PATH, value = CORE.QUICKADD) Boolean quickAdd) {
					System.err.println("quickadd = " + quickAdd);
					if (quickAdd != isSelected())
						setSelected(quickAdd);
					try {
						prefs.putBoolean(CORE.QUICKADD, quickAdd);
						prefs.flush();
					} catch (BackingStoreException e) {
						e.printStackTrace();
					}
				}
			};
			box.addChangeListener(new ChangeListener() {

				@Override
				public void stateChanged(ChangeEvent e) {
					DI.set(CORE.QUICKADD, box.isSelected());
				}
			});
			panel.add(box);
		} catch (IllegalArgumentException | SecurityException ex) {
			Logger.getLogger(Settings.class.getName()).log(Level.SEVERE, null, ex);
		}
		pluginSettings.add(panel);
		pimApi.guiExtensions().stream().forEach(comp -> pluginSettings.add((Component) comp));
	}

	void constructSymbolFields() {
		Field[] fields = ThApplication.class.getDeclaredFields();
		for (Field field : fields) {
			if (field.getName().contains("Symbol") && Modifier.isStatic(field.getModifiers())) {
				symbols.add(new LabelFieldPair(field, 6));
			}
		}
		JButton button = new JButton("reset");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (LabelFieldPair pair : getLabelFieldPairs(symbols)) {
					pair.reset();
				}
			}
		});
		symbols.add(new JLabel(
				"                                                                                                    "));
		symbols.add(button);
	}

	private void createDatePatternGui() {
		Field[] fields = ThApplication.class.getDeclaredFields();
		int i = 0;
		for (Field field : fields) {
			if (field.getName().contains("Pattern") && Modifier.isStatic(field.getModifiers())) {
				if (i == 2) {
					dateTime.add(new JLabel(
							"                                                                                                    "));
				}
				i++;
				LabelFieldPair pair = new LabelFieldPair(field, 8);
				pair.name.setText(pair.name.getText() + field.getName().substring(field.getName().length() - 1));
				dateTime.add(pair);
			}
		}
		dateTime.add(new JLabel(
				"                                                                                                    "));
		JButton button = new JButton("reset");
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (LabelFieldPair pair : getLabelFieldPairs(dateTime)) {
					pair.reset();
				}
			}
		});
		dateTime.add(button);
	}

	List<LabelFieldPair> getLabelFieldPairs(JPanel panel) {
		ArrayList<LabelFieldPair> list = new ArrayList<>();
		for (Component c : panel.getComponents()) {
			if (c instanceof LabelFieldPair) {
				list.add((LabelFieldPair) c);
			}
		}
		return list;
	}

	JComboBox createLocaleSelection() {
		final Locale[] locales = Locale.getAvailableLocales();
		String[] localeStrs = new String[locales.length];
		for (int i = 0; i < locales.length; i++) {
			localeStrs[i] = locales[i].getDisplayName();
		}

		final JComboBox combo = new JComboBox(localeStrs);
		combo.addItemListener(//
		event -> DI.set(CORE.LOCALE, combo.getSelectedItem()));
		combo.setSelectedItem(Locale.getDefault());
		return combo;
	}

}
