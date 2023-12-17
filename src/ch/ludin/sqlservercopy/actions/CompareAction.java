package ch.ludin.sqlservercopy.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.SqlServerCopy;

public class CompareAction extends Action {
	private SqlServerCopy applicationWindow;

	public CompareAction(SqlServerCopy view) {
		setEnabled(false);
		setApplicationWindow(view);
		setText("Compare");
		setToolTipText("Compare");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_TWO_WAY_COMPARE));
	}
	
	@Override
	public void run() {
		getApplicationWindow().compare();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
