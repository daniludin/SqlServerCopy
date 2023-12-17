package ch.ludin.sqlservercopy.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.jface.dialogs.IDialogSettings;

import ch.ludin.sqlservercopy.Activator;

public enum ModelProvider {
	INSTANCE;

	private final static String SETTINGS_KEY = "dbcompare";
	private static final String KEY_PASSWORD = "password";
	private static final String KEY_USERNAME = "username";
	private static final String KEY_DRIVERCLASSNAME = "driverclassname";
	private static final String KEY_URL = "url";
	private static final String KEY_LABEL = "label";
	private static final String KEY_ID = "id";
	private List<ConnectionItem> connectionitems;

	private ModelProvider() {

		connectionitems = new ArrayList<ConnectionItem>();
		loadModel();

	}

	public List<ConnectionItem> getConnectionItems() {
		return connectionitems;
	}

	public void duplicate(ConnectionItem item) {
		ConnectionItem newItem = new ConnectionItem(UUID.randomUUID().toString(), item.getLabel() + " DUPL.", item.getUrl(), item.getUsername(), item.getPassword(), item.getDriverClassName(), true);
		getConnectionItems().add(newItem);
	}
	
	public void delete(ConnectionItem item) {
		ConnectionItem itemToDelete = null;
		for (ConnectionItem p : getConnectionItems()) {
			if (p.getId().equals(item.getId())) {
				itemToDelete = p;
				break;
			}

		}
		getConnectionItems().remove(itemToDelete);
	}

	public void setErrorMessage(String id, String msg, boolean connectionOk) {
		ConnectionItem itemToUpdate = null;
		for (ConnectionItem p : getConnectionItems()) {
			if (p.getId().equals(id)) {
				itemToUpdate = p;
				break;
			}
		}
		itemToUpdate.setConnectionError(msg);
		itemToUpdate.setConnectionStatus(connectionOk);

	}

	public void saveModel() {
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		IDialogSettings settingModel = settings.addNewSection(SETTINGS_KEY);

		for (ConnectionItem item : getConnectionItems()) {
			IDialogSettings settingItem = settingModel.addNewSection(item.getId());
			settingItem.put(KEY_ID, item.getId());
			settingItem.put(KEY_LABEL, item.getLabel());
			settingItem.put(KEY_URL, item.getUrl());
			settingItem.put(KEY_USERNAME, item.getUsername());
			settingItem.put(KEY_PASSWORD, item.getPassword());
			settingItem.put(KEY_DRIVERCLASSNAME, item.getDriverClassName());

		}
		try {
			settings.save(SETTINGS_KEY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void loadModel() {
		getConnectionItems().clear();
		IDialogSettings settings = Activator.getDefault().getDialogSettings();
		if (settings == null) {
			return;
		}

		IDialogSettings setModel = settings.getSection(SETTINGS_KEY);
		if (setModel == null) {
			return;
		}
		IDialogSettings[] teams = setModel.getSections();
		for (int i = 0; i < teams.length; i++) {
			String id = teams[i].get(KEY_ID) != null ? teams[i].get(KEY_ID) : "";
			String label = teams[i].get(KEY_LABEL) != null ? teams[i].get(KEY_LABEL) : "";
			String firstName = teams[i].get(KEY_URL) != null ? teams[i].get(KEY_URL) : "";
			String lastName = teams[i].get(KEY_USERNAME) != null ? teams[i].get(KEY_USERNAME) : "";
			String gender = teams[i].get(KEY_PASSWORD) != null ? teams[i].get(KEY_PASSWORD) : "";
			String driver = teams[i].get(KEY_DRIVERCLASSNAME) != null ? teams[i].get(KEY_DRIVERCLASSNAME) : "";

			ConnectionItem item = new ConnectionItem(id, label, firstName, lastName, gender, driver, true);
			getConnectionItems().add(item);
		}
	}

}
