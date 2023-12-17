package ch.ludin.sqlservercopy.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.SqlServerCopy;

public class SearchDbConfigsAction extends Action {
	private SqlServerCopy applicationWindow;

	public SearchDbConfigsAction(SqlServerCopy view) {
		setApplicationWindow(view);
		setText("Search connections");
		setToolTipText("Searches for connections in all opened projects");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_LOAD));

	}
	
	@Override
	public void run() {
		getApplicationWindow().searchDatabaseConfigs();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
