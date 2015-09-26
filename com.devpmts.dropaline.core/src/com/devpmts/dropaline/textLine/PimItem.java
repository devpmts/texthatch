package com.devpmts.dropaline.textLine;

import java.io.File;
import java.io.Serializable;

import javax.inject.Inject;

import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.util.UniqueId;
import com.devpmts.util.e4.DI;

public class PimItem implements Serializable {

	private long id = UniqueId.get();

	private TitleAndNote titleAndNote = new TitleAndNote();

	private transient PimApi pimApi = (PimApi) DI.get(CORE.DEFAULT_API);

	@Inject
	private transient FileAndDataAccess fileSystemAccess;

	public String getDeliveryMessage() {
		String apiName = getPimApi().name();
		String messageTitle = ((boolean) DI.get(CORE.LEARNING_MODE)) ? "textHatch: LEARNING MODE not sent (" + apiName
				+ ")" : isCached() ? "textHatch: Item cached (" + apiName + ")" : "textHatch: Item successfully sent ("
				+ apiName + ")";
		return messageTitle;
	}

	public PimItem() {
		DI.injectContextIn(this);
	}

	public synchronized long getId() {
		return id;
	}

	public String getUniqueName() {
		return "event " + id;
	}

	public PimApi getPimApi() {
		return pimApi;
	}

	public void setNote(String noteContent) {
		titleAndNote.setNote(noteContent);
	}

	public void setTitle(String line) {
		titleAndNote.setTitle(line);
	}

	public void cacheOnDisk() {
		fileSystemAccess.writeOnDisk(this);
	}

	public String getHumanReadableIdentifier() {
		return "niy";
	}

	public void setPimApi(PimApi pimApi) {
		this.pimApi = pimApi;
	}

	public String getTitle() {
		return titleAndNote.getTitle();
	}

	public String getNote() {
		return titleAndNote.getNote();
	}

	public File getCacheFile() {
		return fileSystemAccess.getFileInDataPath(getUniqueName());
	}

	public boolean isCached() {
		return getCacheFile().exists();
	}

	public void deleteCacheFile() {
		getCacheFile().delete();
	}
}
