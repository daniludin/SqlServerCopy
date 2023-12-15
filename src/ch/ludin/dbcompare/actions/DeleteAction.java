package ch.ludin.dbcompare.actions;

import org.eclipse.jface.action.Action;

import ch.ludin.dbcompare.Activator;
import ch.ludin.dbcompare.SqlServerCopy;

public class DeleteAction extends Action {
	private SqlServerCopy applicationWindow;

	public DeleteAction(SqlServerCopy view) {
		setApplicationWindow(view);
		setText("Delete Connection");
		setToolTipText("Delete Connection");
		setImageDescriptor(Activator.getDefault().getRegistry().getDescriptor(Activator.IMAGE_ID_DELETE));

	}
	
	@Override
	public void run() {
		getApplicationWindow().delete();;
	}

	public SqlServerCopy getApplicationWindow() {
		return applicationWindow;
	}

	public void setApplicationWindow(SqlServerCopy applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	
}
