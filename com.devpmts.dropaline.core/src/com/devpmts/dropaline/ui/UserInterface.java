package com.devpmts.dropaline.ui;

import java.util.List;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import com.devpmts.dropaline.api.AuthenticationException;
import com.devpmts.dropaline.api.PimApi;
import com.devpmts.dropaline.application.ThError;
import com.devpmts.dropaline.textLine.PimItem;

@Singleton
@Creatable
public interface UserInterface {

	static String createNotFoundMessage(Object[] list, String listItemType) {
		String message = "available " + listItemType + "s: " + "\n" + " " + "\n";
		for (int i = 0; i < list.length; i++) {
			message += list[i];
			if (i % 5 == 4) {
				message += " " + "\n";
			} else if (i % 5 < 4 && !(i == list.length - 1)) {
				message += ", ";
			}
		}
		return message;
	}

	void requestCredentialsFromUser(PimApi pimApi);

	void handleSuccessfulDelivery(List<PimItem> items);

	void handleAuthenticationFailed(PimApi api, AuthenticationException a);

	default void handleNotFoundInList(String wasNotFound, Object[] list, String listItemType) {
		String message = createNotFoundMessage(list, listItemType);
		showErrorMessage(ThError.ITEM_NOT_FOUND_IN_LIST, message);
	}

	void preparePimItemTransfer(PimItem item);

	void presentMessageToUser(String itemString, String messageTitle);

	default void showDeliveryMessage(PimItem item) {
		presentMessageToUser(item.toString(), item.getDeliveryMessage());
	}

	default void showErrorMessage(ThError error, String message) {
		presentMessageToUser(null, error.toString() + ": " + message);
	}

	public String getTextLine();

}
