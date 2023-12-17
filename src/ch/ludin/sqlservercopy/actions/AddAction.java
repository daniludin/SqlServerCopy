package ch.ludin.sqlservercopy.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.SqlServerCopy;

public class AddAction extends Action {
	private SqlServerCopy applicationWindow;

	public AddAction(SqlServerCopy view) {
		setApplicationWindow(view);
		setText("Add Connection");
		setToolTipText("Add Connection");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_ADD));
	}
	
	@Override
	public void run() {
		getApplicationWindow().add();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
