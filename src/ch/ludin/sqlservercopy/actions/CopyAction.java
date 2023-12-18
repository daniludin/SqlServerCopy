package ch.ludin.sqlservercopy.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.SqlServerCopy;

public class CopyAction extends Action {
	private SqlServerCopy applicationWindow;

	public CopyAction(SqlServerCopy view) {
		setEnabled(false);
		setApplicationWindow(view);
		setText("Copy");
		setToolTipText("Copy Database");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_COPY));

	}
	
	@Override
	public void run() {
		//getApplicationWindow().list();;
		getApplicationWindow().startDatabaseCopy();;;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
