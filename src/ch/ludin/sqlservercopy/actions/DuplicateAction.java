package ch.ludin.sqlservercopy.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.SqlServerCopy;

public class DuplicateAction extends Action {
	private SqlServerCopy applicationWindow;

	public DuplicateAction(SqlServerCopy view) {
		setApplicationWindow(view);
		setText("Duplicate");
		setToolTipText("Duplicate");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_DUPLICATE));
	}
	
	@Override
	public void run() {
		getApplicationWindow().duplicate();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
