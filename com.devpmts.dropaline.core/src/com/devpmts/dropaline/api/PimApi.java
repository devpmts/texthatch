package com.devpmts.dropaline.api;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.parser.THException;
import com.devpmts.dropaline.parser.ThParser;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.util.e4.DI;

public abstract class PimApi<AUTHENTICATOR extends Authenticator, PIMITEM extends PimItem> {

	private final AUTHENTICATOR authenticator;

	private static List<PimApi> apis = new ArrayList<>();

	public PimApi(AUTHENTICATOR authenticator) {
		this.authenticator = authenticator;
		this.authenticator.setPimApi(this);
		DI.injectContextIn(this.authenticator);
		apis.add(this);
		System.err.println("created " + this.getClass());
	}

	public static Stream<PimApi> getAvailableApis() {
		return apis.stream();
	}

	public AUTHENTICATOR authenticator() {
		return authenticator;
	}

	public String getPropertyKey(String property) {
		// return DevUtilString.toUppercaseWithUnderlines(name()) + "_" +
		// property;
		return property;
	}

	public void setProperty(String key, Object value) {

	}

	public <E extends Object> E getProperty(String key, E defaultValue) {
		return DI.get(getPropertyKey(key), defaultValue);
	}

	public void parse(LineAndNote textHatch) {
		new ThParser(createLineParser(textHatch)).parse();
	}

	public abstract void deleteItem(PIMITEM item);

	public abstract void deliverPimItem(PIMITEM item) throws THException;

	public abstract List<PIMITEM> getEventList();

	public abstract String name();

	protected abstract ItemLineParser createLineParser(LineAndNote textHatch);

	public List<Component> guiExtensions() {
		return Collections.EMPTY_LIST;
	}

}
