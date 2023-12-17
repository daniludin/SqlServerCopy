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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.view.DrawLabelProvider;
import ch.ludin.sqlservercopy.view.LabelLabelProvider;
import ch.ludin.sqlservercopy.view.NullLabelProvider;
import ch.ludin.sqlservercopy.view.UrlLabelProvider;

public class DeploymentDescriptorDialog extends TitleAreaDialog {
	TableViewer viewer;
	Table table;
	List<ConnectionItem> selectedItems = new ArrayList<ConnectionItem>();

	@Override
	protected boolean isResizable() {
		return true;
	}

	List<ConnectionItem> searchDescriptors;

	public DeploymentDescriptorDialog(Shell parentShell) {
		super(parentShell);
	}

	public DeploymentDescriptorDialog(Shell parentShell, List<ConnectionItem> searchDescriptors) {
		super(parentShell);
		this.searchDescriptors = searchDescriptors;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		this.viewer = new TableViewer(container, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWT.CHECK);
		this.table = getViewer().getTable();
		createColumns(parent, viewer);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(3, false));
		layout.addColumnData(new ColumnWeightData(6, false));
		layout.addColumnData(new ColumnWeightData(45, true));
		layout.addColumnData(new ColumnWeightData(85, true));

		getViewer().getTable().setLayout(layout);
		table.setTouchEnabled(true);
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		getViewer().setContentProvider(new DialogContentProvider());
		getViewer().setInput(searchDescriptors);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		return container;
	}

	@Override
	protected void okPressed() {
		getSelectedItems().clear();
		TableItem[] items = table.getItems();
		int length = items.length;
		for (int i = 0; i < length; i++) {
			TableItem item = items[i];
			if (item.getChecked()) {
				getSelectedItems().add((ConnectionItem) item.getData());
			}
		}
		if (selectedItems.size() == 0) {
			return;
		}
		
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
