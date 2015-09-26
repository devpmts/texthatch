package com.textHatch.plugins.apis.catchNotes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.catchnotes.api.CatchAPI;
import com.catchnotes.api.CatchNote;
import com.devpmts.DevsLogger;
import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.parser.ItemLineParser;
import com.devpmts.dropaline.parser.LineAndNote;
import com.devpmts.dropaline.parser.THException;
import com.devpmts.util.e4.DI;

public class CatchPimApi extends PimApi<CatchAuthenticator, CatchPimItem> {

	public static CatchAPI api = new CatchAPI("test-application");

	public CatchPimApi() {
		super(new CatchAuthenticator(api));
	}

	@Override
	public void deliverPimItem(CatchPimItem item) throws THException {
		DevsLogger.log("connecting catch event");
		CatchNote note = new CatchNote();
		note.creationTime = new Date().getTime();
		note.text = item.getTitle();
		note.creationTime = new Date().getTime();
		note.modificationTime = new Date().getTime();
		// note.creationTime=new Date().getTime();
		DevsLogger.log("sending note :  " + item.getTitle());
		if ((boolean) DI.get(CORE.LEARNING_MODE)) {
			return;
		}
		int j = api.addNote(note);
		if (j != 1) {
			throw new THException("SendNote Result was " + j + ", but 1 expected.");
		}
	}

	@Override
	public List<CatchPimItem> getEventList() {
		List<CatchPimItem> list = new ArrayList<>();
		ArrayList<CatchNote> notes = new ArrayList<>();
		api.getNotes(notes);
		for (CatchNote note : notes) {
			CatchPimItem item = new CatchPimItem();
			item.setTitle(note.text.toString());
			item.setTags(Arrays.asList(note.getTags().toString().split(" ")));
			item.setStartTime(new Date(note.modificationTime));
			list.add(item);
		}
		return list;
	}

	@Override
	public String name() {
		return "Catch Notes";
	}

	@Override
	protected ItemLineParser createLineParser(LineAndNote textHatch) {
		return new CatchItemLineParser(textHatch);
	}

	@Override
	public void deleteItem(CatchPimItem item) {
	}

}
