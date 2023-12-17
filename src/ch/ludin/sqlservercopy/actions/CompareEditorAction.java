package ch.ludin.sqlservercopy.actions;

import org.eclipse.compare.CompareUI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import ch.ludin.sqlservercopy.view.CompareInput;

public class CompareEditorAction implements IWorkbenchWindowActionDelegate {
	
	String sbLeft = new String();
	String sbRight = new String();
	
	public CompareEditorAction(String sbLeft, String sbRight) {
		this.sbLeft = sbLeft;
		this.sbRight = sbRight;
	}

	public void run(IAction action) {
		CompareUI.openCompareEditor(new CompareInput(this.sbLeft, this.sbRight), true);
		//CompareUI.openCompareDialog(new CompareInput(this.sbLeft, this.sbRight));
	}

	@Override
	public void selectionChanged(IAction arg0, ISelection arg1) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void init(IWorkbenchWindow arg0) {
	}
}
