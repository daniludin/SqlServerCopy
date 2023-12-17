package ch.ludin.sqlservercopy.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class StringStorage implements IStorage {
	private String string;

	public StringStorage(String input) {
		this.string = input;
	}

	public InputStream getContents() throws CoreException {
		return new ByteArrayInputStream(string.getBytes());
	}

	public IPath getFullPath() {
		return null;
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public String getName() {
		return "DbCompare";

	}

	public boolean isReadOnly() {
		return true;
	}

}
