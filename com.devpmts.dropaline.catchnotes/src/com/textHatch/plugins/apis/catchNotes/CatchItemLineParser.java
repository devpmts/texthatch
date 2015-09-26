package com.textHatch.plugins.apis.catchNotes;

import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.util.e4.DI;

public class CatchItemLineParser extends ItemLineParser {

	protected CatchItemLineParser(LineAndNote textHatch) {
		super(textHatch, "");
	}

	@Override
	public PimItem parseLine() {
		return createNotePimItem(textHatch.noteText);
	}

	private PimItem createNotePimItem(String note) {
		PimItem item = new PimItem();
		item.setPimApi(DI.get(CatchPimApi.class));
		item.setTitle(note);
		return item;
	}

}
