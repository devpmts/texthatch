package com.devpmts.dropaline.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.delivery.CacheDeliveryService;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.dropaline.ui.UserInterface;

@Singleton
@Creatable
public abstract class FileAndDataAccess {

	@Inject
	private UserInterface userInterface;

	public FileAndDataAccess() {
		System.err.println("creating fileAndDataAccess");
	}

	public PimItem writeOnDisk(PimItem item) {
		try (ObjectOutputStream oos = new ObjectOutputStream(getOutputStream(item.getCacheFile()))) {
			oos.writeObject(item);
			DevsLogger.log("cached item " + item.getId());
		} catch (IOException ex) {
			Logger.getLogger(FileAndDataAccess.class.getName()).log(Level.SEVERE, null, ex);
		}
		CacheDeliveryService.trigger();
		userInterface.showDeliveryMessage(item);
		return item;
	}

	{
		getCacheFolder().mkdir();
	}

	public abstract File getCacheFolder();

	public String getDataPath() {
		return getCacheFolder().getName() + File.separator;
	}

	public abstract InputStream getInputStream(File file) throws FileNotFoundException;

	public abstract OutputStream getOutputStream(File file) throws FileNotFoundException;

	public abstract boolean hasOnlineAccess();

	public void handleProblemWithLoadingFile(File file, Exception ex) {
		DevsLogger.log(FileAndDataAccess.class, ex);
		DevsLogger.log("problem with file!");
	}

	public File getFileInDataPath(String fileName) {
		return new File(getDataPath() + fileName);
	}

}
