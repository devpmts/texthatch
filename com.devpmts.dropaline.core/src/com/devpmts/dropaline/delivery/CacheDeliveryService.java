package com.devpmts.dropaline.delivery;

import static com.devpmts.DevsLogger.log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.textLine.PimItem;

public class CacheDeliveryService {

	private static final int INTERVAL_IN_MINUTES = 1;

	private static CacheDeliveryService INSTANCE;

	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Inject
	private FileAndDataAccess fileSystemAccess;

	private CacheDeliveryService() {
	}

	private static CacheDeliveryService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CacheDeliveryService();
		return INSTANCE;
	}

	public static void trigger() {
		getInstance().startIfNotRunning();
	}

	private void startIfNotRunning() {
		if (!scheduler.isShutdown()) {
			log("scheduler already running");
			return;
		}
		log("startup sender service...");
		scheduler.scheduleAtFixedRate(() -> cycle(), 0, CacheDeliveryService.INTERVAL_IN_MINUTES, TimeUnit.MINUTES);
	}

	private void cycle() {
		boolean deliveredAll = attemptToSendAllCachedItems();
		if (deliveredAll) {
			DevsLogger.log("shutting down timer task...");
			scheduler.shutdownNow();
		}
	}

	private boolean attemptToSendAllCachedItems() {
		return ItemDelivery.attemptToSendAllItems(loadCachedItems());
	}

	private List<PimItem> loadCachedItems() {
		List<PimItem> itemList = new ArrayList<>();
		File[] pimItemFiles = fileSystemAccess.getCacheFolder().listFiles();
		if (pimItemFiles.length == 0) {
			return Collections.EMPTY_LIST;
		}
		DevsLogger.log("content of dir " + Arrays.toString(pimItemFiles));
		Arrays.stream(pimItemFiles)//
				.filter(file -> file.getName().contains("event"))//
				.forEach(file -> {
					try {
						itemList.add(readCachedItem(file));
					} catch (Exception ex) {
						fileSystemAccess.handleProblemWithLoadingFile(file, ex);
					}
				});
		return itemList;
	}

	private PimItem readCachedItem(File file) throws ClassNotFoundException, IOException {
		InputStream inputStream = fileSystemAccess.getInputStream(file);
		return (PimItem) new ObjectInputStream(inputStream).readObject();
	}

}
