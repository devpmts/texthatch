//package com.dropaline.gui;
//
//import java.awt.Desktop;
//import java.awt.Dimension;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//import java.io.File;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map.Entry;
//import java.util.TreeSet;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.inject.Inject;
//import javax.inject.Singleton;
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.JButton;
//import javax.swing.JCheckBox;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JTextField;
//import javax.swing.event.PopupMenuEvent;
//import javax.swing.event.PopupMenuListener;
//
//import org.eclipse.e4.core.di.annotations.Creatable;
//import org.mcavallo.opencloud.Cloud;
//import org.mcavallo.opencloud.Tag;
//import org.netbeans.lib.awtextra.AbsoluteConstraints;
//import org.netbeans.lib.awtextra.AbsoluteLayout;
//
//import com.devpmts.DevsLogger;
//import com.devpmts.util.e4.DI;
//// imcom.devpmts.dropaline.corealine.core.application.ThAppContext;
//import com.dropaline.plugins.apis.toodleDo.Symbols;
//import com.dropaline.plugins.apis.toodleDo.ToodledoPimApi;
//
//@Singleton
//@Creatable
//public class ChangeTag<TASK extends Object> extends JPanel {
//
//	private static final String EXCLUDE_TAGS = DI.getString("excludeTags");
//
//	private static final String TAG_CLOUD_THRESHOLD = DI.getString("TagCloud_threshold");
//
//	private static final String INCLUDE_TAGS = DI.getString("includeTags");
//
//	private static final String INCLUDE_COMPLETED = DI.getString("includeCompleted");
//
//	private JButton apply;
//
//	private JButton applyExclude;
//
//	private JButton applyInclude;
//
//	private JButton applyThreshold;
//
//	private JTextField excludeTags;
//
//	private JCheckBox includeCompleted;
//
//	private JTextField includeTags;
//
//	private JButton jButton1;
//
//	private JButton jButton2;
//
//	private JButton jButton3;
//
//	private JButton jButton4;
//
//	private JButton jButton5;
//
//	private JLabel jLabel1;
//
//	private JLabel jLabel2;
//
//	private JLabel jLabel3;
//
//	private JTextField newFolder;
//
//	private JTextField newTagName;
//
//	private JButton openCloud;
//
//	private JComboBox tag;
//
//	private JTextField threshold;
//
//	{
//		tag = new JComboBox();
//		newTagName = new JTextField();
//		apply = new JButton();
//		includeCompleted = new JCheckBox();
//		openCloud = new JButton();
//		jButton1 = new JButton();
//		excludeTags = new JTextField();
//		includeTags = new JTextField();
//		jLabel1 = new JLabel();
//		jLabel2 = new JLabel();
//		applyInclude = new JButton();
//		applyExclude = new JButton();
//		jButton2 = new JButton();
//		threshold = new JTextField();
//		jLabel3 = new JLabel();
//		applyThreshold = new JButton();
//		jButton3 = new JButton();
//		newFolder = new JTextField();
//		jButton4 = new JButton();
//		jButton5 = new JButton();
//	}
//
//	@Inject
//	ToodledoPimApi taskApi;
//
//	ArrayList<TASK> tasks;
//
//	TreeSet<String> tagSet;
//
//	private boolean tagsLower;
//
//	@Inject
//	public ChangeTag() {
//		DI.injectContextIn(this);
//		initComponents();
//	}
//
//	public void init() {
//		taskApi.includeCompletedTasks = DI.get(INCLUDE_COMPLETED, false);
//		includeCompleted.setSelected(taskApi.includeCompletedTasks);
//		includeTags.setText(DI.getString(INCLUDE_TAGS));
//		applyIncludes();
//		excludeTags.setText(DI.get(EXCLUDE_TAGS).toString());
//		applyExcludes();
//		Double thresDouble = DI.get(TAG_CLOUD_THRESHOLD, 0d);
//		threshold.setText(thresDouble.toString());
//	}
//
//	private void initComponents() {
//
//		setAlignmentX(0.0F);
//		setPreferredSize(new Dimension(300, 350));
//		setLayout(new AbsoluteLayout());
//
//		tag.addMouseListener(new MouseAdapter() {
//
//			@Override
//			public void mousePressed(MouseEvent evt) {
//			}
//		});
//		tag.addPopupMenuListener(new PopupMenuListener() {
//
//			@Override
//			public void popupMenuCanceled(PopupMenuEvent evt) {
//			}
//
//			@Override
//			public void popupMenuWillBecomeInvisible(PopupMenuEvent evt) {
//			}
//
//			@Override
//			public void popupMenuWillBecomeVisible(PopupMenuEvent evt) {
//				if (tag.getModel().getSize() == 0) {
//					populateTags();
//				}
//			}
//		});
//		add(tag, new AbsoluteConstraints(10, 30, 117, -1));
//		add(newTagName, new AbsoluteConstraints(130, 30, 111, -1));
//
//		apply.setText("apply");
//		apply.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				HashMap<String, String> changes = new HashMap<>();
//				if (!((String) tag.getSelectedItem()).isEmpty() && newTagName.getText() != null) {
//					changes.put(tag.getSelectedItem().toString(), newTagName.getText());
//					taskApi.changeTag(changes, (ArrayList<com.maldworth.toodledo.response.models.Task>) tasks);
//					Object[] tagArray = tagSet.toArray();
//					tagSet.clear();
//					for (int i = 0; i < tagArray.length; i++) {
//						for (Entry<String, String> entry : changes.entrySet()) {
//							if (entry.getKey().equals(tagArray[i])) {
//								tagArray[i] = entry.getValue();
//							}
//						}
//						tagSet.add((String) tagArray[i]);
//					}
//					Object[] tmp = tagSet.toArray();
//					int index = Arrays.asList(tmp).indexOf(changes.entrySet().iterator().next().getValue());
//					populateTags();
//					if (index > 0) {
//						tag.setSelectedIndex(index);
//					}
//					newTagName.setText("");
//				}
//			}
//		});
//		add(apply, new AbsoluteConstraints(240, 30, 70, -1));
//
//		includeCompleted.setText("include Completed");
//		includeCompleted.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				boolean bool = includeCompleted.isSelected();
//				taskApi.includeCompletedTasks = bool;
//				DI.set(INCLUDE_COMPLETED, bool);
//			}
//		});
//		add(includeCompleted, new AbsoluteConstraints(240, 0, -1, -1));
//
//		openCloud.setText("open Cloud");
//		openCloud.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				openCloud();
//			}
//		});
//		add(openCloud, new AbsoluteConstraints(120, 0, 117, -1));
//
//		jButton1.setText("connect Tasks with contactNotes via Tags");
//		jButton1.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//			}
//		});
//		add(jButton1, new AbsoluteConstraints(0, 320, 262, -1));
//		add(excludeTags, new AbsoluteConstraints(10, 180, 180, -1));
//		add(includeTags, new AbsoluteConstraints(10, 120, 180, -1));
//
//		jLabel1.setText("include Tags (Comma-separated):");
//		add(jLabel1, new AbsoluteConstraints(10, 100, -1, -1));
//
//		jLabel2.setText("exclude Tags (Comma-separated):");
//		add(jLabel2, new AbsoluteConstraints(10, 160, -1, -1));
//
//		applyInclude.setText("apply");
//		applyInclude.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				applyIncludes();
//			}
//		});
//		add(applyInclude, new AbsoluteConstraints(190, 120, -1, -1));
//
//		applyExclude.setText("apply");
//		applyExclude.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				DevsLogger.log(ChangeTag.this.excludeTags.getText());
//				applyExcludes();
//			}
//		});
//		add(applyExclude, new AbsoluteConstraints(190, 180, -1, -1));
//
//		jButton2.setText("populate Tags");
//		jButton2.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				populateTags();
//			}
//		});
//		add(jButton2, new AbsoluteConstraints(0, 0, -1, -1));
//		add(threshold, new AbsoluteConstraints(130, 220, 60, -1));
//
//		jLabel3.setText("tag threshold");
//		add(jLabel3, new AbsoluteConstraints(40, 220, -1, -1));
//
//		applyThreshold.setText("apply");
//		applyThreshold.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				DI.set(TAG_CLOUD_THRESHOLD, threshold.getText());
//				DevsLogger.log("threshold changed to " + DI.get(TAG_CLOUD_THRESHOLD, 0d));
//			}
//		});
//		add(applyThreshold, new AbsoluteConstraints(190, 220, -1, -1));
//
//		jButton3.setText("get Lower threshold Tags");
//		jButton3.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				ChangeTag.this.getLowerThresholdTags();
//			}
//		});
//		add(jButton3, new AbsoluteConstraints(50, 250, -1, -1));
//		add(newFolder, new AbsoluteConstraints(130, 60, 110, -1));
//
//		jButton4.setText("apply");
//		jButton4.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				if (!((String) tag.getSelectedItem()).isEmpty() && !newFolder.getText().isEmpty()) {
//					populateTags();
//					taskApi.tag2Folder(tag.getSelectedItem().toString(), newFolder.getText(),
//							(ArrayList<com.maldworth.toodledo.response.models.Task>) tasks);
//				}
//			}
//		});
//		add(jButton4, new AbsoluteConstraints(240, 60, -1, -1));
//
//		jButton5.setText("remove duplicate Tasks");
//		jButton5.addActionListener(new ActionListener() {
//
//			@Override
//			public void actionPerformed(ActionEvent evt) {
//				taskApi.cleanTags();
//			}
//		});
//		add(jButton5, new AbsoluteConstraints(40, 280, -1, -1));
//	}
//
//	public void openCloud() {
//		populateTags();
//		try {
//			int count = 0;
//			int countTasks = taskApi.taskCount;
//			int countUsage = 0;
//			List<Tag> tags = getCloudTags();
//			String az = "\"";
//			String str = "<html><head>";
//
//			// str +=
//			// "<meta http-equiv="+az+"content-type"+az+" content="+az+"text/html; charset=Unicode"+az+">";
//			str += "</head><body><p style=" + az + "text-align:center; font-size:40px;" + az + ">";
//			str += "<a href=http://www.toodledo.com/views/folder.php>folder</a>        ";
//			str += "<a href=http://www.toodledo.com/views/index.php?i=0>hotlist</a>        ";
//			str += "<a href=http://www.toodledo.com/views/index.php?i=4>starred</a>        ";
//			str += "<a href=http://www.toodledo.com/views/search.php?i=9829>inbox</a>        ";
//			str += "<br><br>";
//			str += "<style type="
//					+ az
//					+ "text/css"
//					+ az
//					+ "> A:link { text-decoration: none margin-left:10px } A:visited {text-decoration: none} A:active {text-decoration: none} A:hover {text-decoration: underline; color: red;} </style>";
//			String strBig = "";
//			for (Tag tagTmp : tags) {
//				if (tagsLower && !taskApi.tagIncludeSet.contains(tagTmp.getName().trim())) {
//					DevsLogger.log("skipping..." + tagTmp.getName());
//					countTasks--;
//					continue;
//				}
//				count++;
//				countUsage += tagTmp.getScoreInt();
//				boolean big = false;
//				tagTmp.setLink("http://www.toodledo.com/views/search.php?i=-9&xx=1340583&numrules=2&numgroups=0&andor=1&mygroup1=0&field1=2&type11=6&type21=6&type31=6&type41=6&type51=6&type61=6&mygroup2=0&field2=18&type12=1&type22=2&type32=2&type42=2&type52=2&type62=2&value2="
//						+ tagTmp.getName() + "&search=1");
//				// double weight = tag.getWeight();
//				// if (weight > 0.06) {
//				// weight = 0.06 + ((weight - 0.06) * 1 / 10);
//				// }
//				// if (weight > 1) {
//				// big = true;
//				// }
//				// if (weight < 0.06) {
//				// weight = 0.06;
//				// }
//				int score = (tagTmp.getScoreInt());
//				score = (score < 50) ? score : 50;
//				score = score * 1 / 3 + 13;
//				// if (tag.getWeight() <= 1) {
//				// score = 15 + 15 * tag.getWeightInt();
//				// }
//				String tmp = " <a href=" + tagTmp.getLink() + " style=" + az + "font-size: " + score + "px" + az + ";>"
//						+ tagTmp.getName() + "(" + tagTmp.getScoreInt() + ") </a> ";
//				tmp += "<a style=" + az + "font-size:12px" + az + "; text-color:white!important;></a> ";
//
//				// + "("+ tag.getScoreInt() + ")</a> ";
//				if (big) {
//					strBig += tmp;
//				} else {
//					str += tmp;
//				}
//			}
//			str += strBig;
//			str += "<p style=" + az + "color:#ccc; text-align:left; font-size:15px;" + az + ">";
//			str += "<br>";
//			str += "number of Tags: " + count + "<br>";
//			str += "number of all Tasks: " + countTasks + "<br>";
//			str += "number of Usages: " + countUsage + "<br>";
//			if (!tagsLower) {
//				str += "included Tags: " + taskApi.tagExcludeSet + "<br>";
//			}
//			str += "excluded Tags: " + taskApi.tagIncludeSet + "<br>";
//			str += "</p></body></html>";
//			File file = new File("tagCloud.html");
//			FileWriter fw = new FileWriter(file);
//			fw.write(str);
//			fw.close();
//			Desktop.getDesktop().open(file);
//		} catch (IOException ex) {
//			Logger.getLogger(ChangeTag.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
//
//	public void getTagCloud(String text) {
//		text = text.substring(Symbols.SYMBOL_TAG_CLOUD.length());
//		taskApi.tagIncludeSet.clear();
//		tag.setModel(new DefaultComboBoxModel());
//		taskApi.tagIncludeSet.addAll(Arrays.asList(text.split(",")));
//		openCloud();
//	}
//
//	private void populateTags() {
//		DevsLogger.log("populating tags");
//		tasks = (ArrayList<TASK>) taskApi.getTasks();
//		tagSet = taskApi.getTags((ArrayList<com.maldworth.toodledo.response.models.Task>) tasks);
//		Object[] tags = tagSet.toArray();
//		tag.setModel(new DefaultComboBoxModel(tags));
//	}
//
//	private void applyIncludes() {
//		getTaskPimApi().tagIncludeSet.clear();
//		getTaskPimApi().tagIncludeSet.addAll(Arrays.asList(includeTags.getText().split((","))));
//		DI.set(INCLUDE_TAGS, getTaskPimApi().tagIncludeSet);
//	}
//
//	private void applyExcludes() {
//		getTaskPimApi().tagExcludeSet.clear();
//		getTaskPimApi().tagExcludeSet.addAll(Arrays.asList(excludeTags.getText().split((","))));
//		DI.set(EXCLUDE_TAGS, getTaskPimApi().tagExcludeSet);
//	}
//
//	private ToodledoPimApi getTaskPimApi() {
//		return DI.get(ToodledoPimApi.class);
//	}
//
//	List<Tag> getCloudTags() {
//		Cloud cloud = new Cloud();
//		cloud.setThreshold(DI.get(TAG_CLOUD_THRESHOLD, 0d));
//		cloud.setMaxTagsToDisplay(500);
//		cloud.addText(taskApi.tagText);
//		DevsLogger.log(taskApi.tagText);
//		return cloud.tags();
//	}
//
//	void getLowerThresholdTags() {
//		Double d = DI.get(TAG_CLOUD_THRESHOLD, 0d);
//		taskApi.tagIncludeSet.clear();
//		DI.set(TAG_CLOUD_THRESHOLD, d + 1d);
//		populateTags();
//		List<Tag> listUpper = getCloudTags();
//		DI.set(TAG_CLOUD_THRESHOLD, 0d);
//		List<Tag> listLower = getCloudTags();
//		List<String> strUp = new ArrayList<>();
//		List<String> strLow = new ArrayList<>();
//		for (Tag tag : listLower) {
//			strLow.add(tag.getName());
//		}
//		for (Tag tag : listUpper) {
//			strUp.add(tag.getName());
//		}
//		for (String tagLow : strLow) {
//			if (!strUp.contains(tagLow) && !tagLow.equals("")) {
//				taskApi.tagIncludeSet.add(tagLow);
//			}
//		}
//		tagsLower = true;
//		openCloud();
//		tagsLower = false;
//		taskApi.tagIncludeSet.clear();
//		DI.set(TAG_CLOUD_THRESHOLD, d);
//	}
// }
