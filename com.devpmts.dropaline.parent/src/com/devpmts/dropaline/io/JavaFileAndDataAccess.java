package com.devpmts.dropaline.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.io.FileAndDataAccess;

public class JavaFileAndDataAccess extends FileAndDataAccess {

	@Override
	public InputStream getInputStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

	@Override
	public OutputStream getOutputStream(File file) throws FileNotFoundException {
		return new FileOutputStream(file);
	}

	@Override
	public File getCacheFolder() {
		return new File("eventCache");
	}

	@Override
	public boolean hasOnlineAccess() {
		try {
			URL url = new URL("http://www.google.com");
			url.openConnection();
			// TextHatchLogger.log("Internet Connection available");
		} catch (Exception e) {
			DevsLogger.log("No connection to internet!");
			return false;
		}
		return true;
	}

}
