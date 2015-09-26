package com.devpmts.dropaline.delivery;

import java.util.List;

import javax.inject.Inject;

import com.devpmts.DevsLogger;
import com.devpmts.dropaline.CORE;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.io.FileAndDataAccess;
import com.devpmts.dropaline.parser.THException;
import com.devpmts.dropaline.textLine.PimItem;
import com.devpmts.dropaline.ui.UserInterface;
import com.devpmts.util.e4.DI;

public class ItemDelivery {

	public static boolean attemptToSendAllItems(List<PimItem> toDeliver) {
		return new ItemDelivery().attempt(toDeliver);
	}

	@Inject
	private FileAndDataAccess fileSystemAccess;

	private ItemDelivery() {
		DI.injectContextIn(this);
	}

	@Inject
	private UserInterface userInterface;

	public boolean attempt(List<PimItem> toDeliver) {
		if (!weAreOnline()) {
			return false;
		}
		DevsLogger.log("trying to send, if there are any cached Events...");
		return toDeliver.stream()//
				.filter(item -> attemptDelivery(item)).count() == toDeliver.size();
	}

	private boolean weAreOnline() {
		return fileSystemAccess.hasOnlineAccess();
	}

	public boolean attemptDelivery(PimItem item) {
		PimApi pimApi = item.getPimApi();
		if (pimApi.authenticator().isAuthenticated()) {
			try {
				pimApi.deliverPimItem(item);
				if (!((boolean) DI.get(CORE.LEARNING_MODE)))
					item.deleteCacheFile();
				userInterface.showDeliveryMessage(item);
				return true;
			} catch (THException e) {
				DevsLogger.log(CacheDeliveryService.class, e);
			}
		}
		item.cacheOnDisk();
		return false;
	}

}
