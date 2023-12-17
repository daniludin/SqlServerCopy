package ch.ludin.sqlservercopy.model;

import org.eclipse.core.resources.IStorage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IStorageEditorInput;

import ch.ludin.sqlservercopy.Activator;

public class StringInput implements IStorageEditorInput {
	private IStorage storage;

	public StringInput(IStorage storage) {
		this.storage = storage;
	}

	public boolean exists() {
		return true;
	}

	public ImageDescriptor getImageDescriptor() {
		return Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_LOGO_SQLSERVER);
		
	}

	public String getName() {
		return storage.getName();
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public IStorage getStorage() {
		return storage;
	}

	public String getToolTipText() {
		return "Db Struktur: " + storage.getName();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

}
