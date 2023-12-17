package ch.ludin.sqlservercopy.test;

import java.util.Comparator;
import java.util.function.Function;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerSorter {

	private static final String DATA_COMPARATOR = "comparator";

	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());

		final TableViewer viewer = new TableViewer(shell, SWT.BORDER);
		Table table = viewer.getTable();
		table.setHeaderVisible(true);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setSorter(new ViewerSorter() {

			@Override
			public int compare(Viewer v, Object e1, Object e2) {
				TableColumn sortColumn = table.getSortColumn();
				Comparator comparator = sortColumn == null ? null : (Comparator) sortColumn.getData(DATA_COMPARATOR);
				if (comparator != null && table.getSortDirection() == SWT.UP) {
					comparator = comparator.reversed();
				}
				return comparator == null ? 0 : comparator.compare(e1, e2);
			}
		});

		createTableViewerColumn(viewer, "ID", v -> v.ordinal());
		createTableViewerColumn(viewer, "Name", v -> v.getDisplayName());
		createTableViewerColumn(viewer, "Fruit", v -> v.isFruit());

		for (TableColumn column : table.getColumns()) {
			column.addListener(SWT.Selection, e -> {
				final TableColumn sortColumn = table.getSortColumn();
				int direction = table.getSortDirection();

				if (column.equals(sortColumn)) {
					direction = direction == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {
					table.setSortColumn(column);
					direction = SWT.UP;
				}
				table.setSortDirection(direction);
				viewer.refresh();
			});
		}
		viewer.setInput(Value.values());

		shell.pack();
		shell.open();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	private static <T extends Comparable<T>> TableViewerColumn createTableViewerColumn(TableViewer viewer, String name,
			Function<Value, T> propertyFunction) {
		TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		viewerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return propertyFunction.apply((Value) element).toString();
			}
		});

		TableColumn column = viewerColumn.getColumn();
		column.setText(name);
		column.setWidth(100);
		column.setData(DATA_COMPARATOR,
				(Comparator<Value>) (v1, v2) -> propertyFunction.apply(v1).compareTo(propertyFunction.apply(v2)));
		return viewerColumn;
	}

	static enum Value {
		APPLE, BANANA, CABBAGE, DILL, EGGPLANT;

		String getDisplayName() {
			return name().substring(0, 1) + name().substring(1).toLowerCase();
		}

		boolean isFruit() {
			return this != CABBAGE && this != DILL;
		}
	}
}