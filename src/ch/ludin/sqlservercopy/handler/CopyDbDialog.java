package ch.ludin.sqlservercopy.handler;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.view.DrawLabelProvider;
import ch.ludin.sqlservercopy.view.LabelLabelProvider;
import ch.ludin.sqlservercopy.view.NullLabelProvider;
import ch.ludin.sqlservercopy.view.UrlLabelProvider;

public class CopyDbDialog extends TitleAreaDialog {
	TableViewer viewer;
	Table table;
	List<ConnectionItem> selectedItems = new ArrayList<ConnectionItem>();

	ConnectionItem conItem1;
	ConnectionItem conItem2;
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	List<ConnectionItem> searchDescriptors;

	public CopyDbDialog(Shell parentShell) {
		super(parentShell);
	}

	public CopyDbDialog(Shell parentShell, ConnectionItem conItem1, ConnectionItem conItem2) {
		super(parentShell);
		this.conItem1 = conItem1;
		this.conItem2 = conItem2;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

//		this.viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK);
//		this.table = getViewer().getTable();
//		createColumns(parent, viewer);
//
//		TableLayout layout = new TableLayout();
//		layout.addColumnData(new ColumnWeightData(3, false));
//		layout.addColumnData(new ColumnWeightData(6, false));
//		layout.addColumnData(new ColumnWeightData(45, true));
//		layout.addColumnData(new ColumnWeightData(85, true));
//
//		getViewer().getTable().setLayout(layout);
//		table.setTouchEnabled(true);
//		table.setLinesVisible(true);
//		table.setHeaderVisible(true);
//
//		getViewer().setContentProvider(new DialogContentProvider());
//		getViewer().setInput(searchDescriptors);
//
//		// Layout the viewer
//		GridData gridData = new GridData();
//		gridData.verticalAlignment = GridData.FILL;
//		gridData.horizontalSpan = 2;
//		gridData.grabExcessHorizontalSpace = true;
//		gridData.grabExcessVerticalSpace = true;
//		gridData.horizontalAlignment = GridData.FILL;
//		viewer.getControl().setLayoutData(gridData);

		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		//gridData.horizontalSpan = 2;
		//gridData.grabExcessHorizontalSpace = true;
		//gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		gridData.verticalIndent = 20;
		gridData.horizontalIndent = 20;
		
		Group group = new Group(container, SWT.SHADOW_ETCHED_IN);
	    group.setText("Copy Database");
	    group.setLayoutData(gridData);

	    Label label = new Label(group, SWT.NONE);
	    label.setText("Source Database:");
	    label.setLocation(20, 20);
	    label.pack();

	    Label labelSource = new Label(group, SWT.NONE);
	    labelSource.setText(conItem1.getUrl());
	    labelSource.setLocation(120, 20);
	    labelSource.pack();

	    Label label2 = new Label(group, SWT.NONE);
	    label2.setText("Target Database:");
	    label2.setLocation(20, 40);
	    label2.pack();

	    Label labelTarget = new Label(group, SWT.NONE);
	    labelTarget.setText(conItem2.getUrl());
	    labelTarget.setLocation(120, 40);
	    labelTarget.pack();

	    Button button1 = new Button(group, SWT.PUSH);
	    button1.setText("Swap Source/Target");
	    button1.setLocation(600, 20);
	    button1.pack();

	    button1.addListener(SWT.Selection, new Listener() {
	    	boolean toggle = false;
	        public void handleEvent(Event e) {
	          switch (e.type) {
	          case SWT.Selection:
	            System.out.println("Button pressed");
	            if (toggle) {
					toggle = false;
					labelSource.setText(conItem1.getUrl());
					labelTarget.setText(conItem2.getUrl());
				} else {
					toggle = true;
					labelSource.setText(conItem2.getUrl());
					labelTarget.setText(conItem1.getUrl());
				}
	            labelSource.pack();
	            labelTarget.pack();
	            
	            break;
	          }
	        }
	      });
		
		
//		Label label1 = new Label(container, SWT.FILL);
//		label1.setText(conItem1.getUrl());
		
		return container;
	}

	@Override
	protected void okPressed() {
//		getSelectedItems().clear();
//		TableItem[] items = table.getItems();
//		int length = items.length;
//		for (int i = 0; i < length; i++) {
//			TableItem item = items[i];
//			if (item.getChecked()) {
//				getSelectedItems().add((ConnectionItem) item.getData());
//			}
//		}
//		if (selectedItems.size() == 0) {
//			return;
//		}
		
		super.okPressed();
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "", "", "Label", "JDBC Url" };
		int[] bounds = { 1, 1, 10, 10 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new NullLabelProvider(null));

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setLabelProvider(new DrawLabelProvider());

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setLabelProvider(new LabelLabelProvider(null));

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setLabelProvider(new UrlLabelProvider(null));


	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;

	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("JDBC Connection Strings extrahiert aus Deployment Descriptors");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(800, 350);
	}

	public TableViewer getViewer() {
		return viewer;
	}

	public List<ConnectionItem> getSelectedItems() {
		return selectedItems;
	}

}
