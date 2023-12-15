package ch.ludin.dbcompare.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.dbcompare.Activator;
import ch.ludin.dbcompare.SqlServerCopy;

public class ListAction extends Action {
	private SqlServerCopy applicationWindow;

	public ListAction(SqlServerCopy view) {
		setEnabled(false);
		setApplicationWindow(view);
		setText("List");
		setToolTipText("List");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_LIST));

	}
	
	@Override
	public void run() {
		getApplicationWindow().list();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
