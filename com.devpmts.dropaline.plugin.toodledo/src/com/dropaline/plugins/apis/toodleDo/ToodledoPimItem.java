package com.dropaline.plugins.apis.toodleDo;

import java.util.Calendar;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.loststone.toodledo.util.TdDate;

import com.devpmts.dropaline.textLine.PimItem;

public class ToodledoPimItem extends PimItem {

	String context = "";

	int priority = 0;

	String project = "";

	boolean starred;

	String status = "";

	Set<String> tags = new TreeSet<>();

	public String getContext() {
		return context;
	}

	public int getPriority() {
		return priority;
	}

	public String getProject() {
		return project;
	}

	public String getStatus() {
		return status;
	}

	public Set<String> getTags() {
		return tags;
	}

	public boolean isStarred() {
		return starred;
	}

	public void setContext(String string) {
		this.context = string;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setStarred(boolean starred) {
		this.starred = starred;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setTags(Collection<String> tagCollection) {
		tags.clear();
		tags.addAll(tagCollection);
	}

	public boolean isAllDay() {
		return false;
	}

	public Calendar getDueTime() {
		return null;
	}

	public String getDueDate() {
		return null;
	}

	public void setStartTime(TdDate tdDate) {
	}

	public void setDueTime(int i) {
	}

	public void setDueDate(int i) {
	}

	public void setAllDay(boolean b) {
	}

	public void setDateValid(boolean b) {
	}

	public int getStartTime() {
		return 0;
	}

	public void setContextId(int id) {
	}

}