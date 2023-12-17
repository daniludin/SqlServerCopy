package ch.ludin.sqlservercopy;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.part.ViewPart;

public class DbCompareViewTest extends ViewPart {
	Label label;
	TableViewer viewer;
	
	public DbCompareViewTest() {
		super();
	}

	@Override
	public void createPartControl(Composite parent) {

	    Composite tableParent = new Composite(parent, SWT.NONE);
	    final GridData layoutData = new GridData(GridData.FILL_BOTH);
	    layoutData.heightHint = 150;
	    tableParent.setLayoutData(layoutData);
	    TableColumnLayout tableLayout = new TableColumnLayout();
	    tableParent.setLayout(tableLayout);
	    viewer = new TableViewer(tableParent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
		
	}

	@Override
	public void setFocus() {
		label.setFocus();
	}

}
