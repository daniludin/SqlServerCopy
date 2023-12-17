package ch.ludin.sqlservercopy;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.eclipse.core.resources.IStorage;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import ch.ludin.sqlservercopy.actions.AddAction;
import ch.ludin.sqlservercopy.actions.CompareAction;
import ch.ludin.sqlservercopy.actions.CompareEditorAction;
import ch.ludin.sqlservercopy.actions.DeleteAction;
import ch.ludin.sqlservercopy.actions.DuplicateAction;
import ch.ludin.sqlservercopy.actions.ListAction;
import ch.ludin.sqlservercopy.actions.SearchDbConfigsAction;
import ch.ludin.sqlservercopy.handler.DeploymentDescriptorDialog;
import ch.ludin.sqlservercopy.handler.DeploymentDescriptorHandler;
import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.model.CustomSQLException;
import ch.ludin.sqlservercopy.model.DatabaseStructureReader;
import ch.ludin.sqlservercopy.model.DatabaseStructureReaderFactory;
import ch.ludin.sqlservercopy.model.ModelProvider;
import ch.ludin.sqlservercopy.model.StringInput;
import ch.ludin.sqlservercopy.model.StringStorage;
import ch.ludin.sqlservercopy.view.ConnectionViewerComparator;
import ch.ludin.sqlservercopy.view.DrawLabelProvider;
import ch.ludin.sqlservercopy.view.ErrorLabelProvider;
import ch.ludin.sqlservercopy.view.LabelEditingSupport;
import ch.ludin.sqlservercopy.view.LabelLabelProvider;
import ch.ludin.sqlservercopy.view.PasswordEditingSupport;
import ch.ludin.sqlservercopy.view.PasswordLabelProvider;
import ch.ludin.sqlservercopy.view.StatusLabelProvider;
import ch.ludin.sqlservercopy.view.UrlEditingSupport;
import ch.ludin.sqlservercopy.view.UrlLabelProvider;
import ch.ludin.sqlservercopy.view.UsernameEditingSupport;
import ch.ludin.sqlservercopy.view.UsernameLabelProvider;

public class SqlServerCopy extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "ch.ludin.sqlservercopy.SqlServerCopy";
	@Inject
	IWorkbench workbench;
	private SearchDbConfigsAction testAction = new SearchDbConfigsAction(this);
	private DuplicateAction duplicateAction = new DuplicateAction(this);
	private AddAction addAction = new AddAction(this);
	private DeleteAction deleteAction = new DeleteAction(this);
	private CompareAction compareAction = new CompareAction(this);
	private ListAction listAction = new ListAction(this);
	private TableViewer viewer;
	private IStructuredSelection selection;
	StringBuffer sbLeft = null;
	StringBuffer sbRight = null;
	private ConnectionViewerComparator comparator;

	public void createPartControl(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		parent.setLayout(layout);
		ModelProvider.INSTANCE.loadModel();
		createViewer(parent);
		contributeToActionBars();
		hookContextMenu();

	}

	private void createViewer(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		createColumns(parent, viewer);

		final Table table = viewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableLayout layout = new TableLayout();
		layout.addColumnData(new ColumnWeightData(2, false));
		layout.addColumnData(new ColumnWeightData(15, true));
		layout.addColumnData(new ColumnWeightData(35, true));
		layout.addColumnData(new ColumnWeightData(15, true));
		layout.addColumnData(new ColumnWeightData(15, true));
		layout.addColumnData(new ColumnWeightData(3, false));
		layout.addColumnData(new ColumnWeightData(24, true));

		viewer.getTable().setLayout(layout);
		viewer.setContentProvider(new ArrayContentProvider());
		viewer.setInput(ModelProvider.INSTANCE.getConnectionItems());
		getSite().setSelectionProvider(viewer);
		comparator = new ConnectionViewerComparator();
		viewer.setComparator(comparator);

		// Layout the viewer
		GridData gridData = new GridData();
		gridData.verticalAlignment = GridData.FILL;
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.horizontalAlignment = GridData.FILL;
		viewer.getControl().setLayoutData(gridData);

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setSelection(viewer.getStructuredSelection());
				if (getSelection().size() == 2) {
					compareAction.setEnabled(true);
				} else {
					compareAction.setEnabled(false);
				}
				if (getSelection().size() == 1) {
					listAction.setEnabled(true);
					duplicateAction.setEnabled(true);
				} else {
					listAction.setEnabled(false);
					duplicateAction.setEnabled(false);
				}

			}
		});

		viewer.getControl().addControlListener(new ControlListener() {

	        @Override
	        public void controlResized(ControlEvent arg0) {
	            Rectangle rect = viewer.getTable().getClientArea();
	            if(rect.width>0){
	                int extraSpace=rect.width/(table.getColumnCount() - 1);

					table.getColumns()[0].setWidth((rect.width/100) * 3);
					table.getColumns()[1].setWidth((rect.width/100) * 15);
					table.getColumns()[2].setWidth((rect.width/100) * 35);
					table.getColumns()[3].setWidth((rect.width/100) * 15);
					table.getColumns()[4].setWidth((rect.width/100) * 15);
					table.getColumns()[5].setWidth((rect.width/100) * 3);
					table.getColumns()[6].setWidth((rect.width/100) * 24);

	            }
	        }

	        @Override
	        public void controlMoved(ControlEvent arg0) {
	            // TODO Auto-generated method stub

	        }
	    });

	}
	
//	  private void layoutTableColumns()
//	  {
//	    // Resize the columns to fit the contents
//	    conceptColumn.getColumn().pack();
//	    stylesheetColumn.getColumn().pack();
//	    // Use the packed widths as the minimum widths
//	    int stylesheetWidth = stylesheetColumn.getColumn().getWidth();
//	    int conceptWidth = conceptColumn.getColumn().getWidth();
//	    // Set stylesheet column to fill 100% and concept column to fit 0%, but with their packed widths as minimums
//	    tableLayout.setColumnData(stylesheetColumn.getColumn(), new ColumnWeightData(100, stylesheetWidth));
//	    tableLayout.setColumnData(conceptColumn.getColumn(), new ColumnWeightData(0, conceptWidth));
//	  }

	public TableViewer getViewer() {
		return viewer;
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				SqlServerCopy.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void fillContextMenu(IMenuManager manager) {
		if (getSelection().size() == 2) {
			manager.add(compareAction);
		}
		if (getSelection().size() == 1) {
			manager.add(listAction);
		}
		manager.add(deleteAction);
		manager.add(addAction);
		manager.add(duplicateAction);

		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(compareAction);
		manager.add(addAction);
		manager.add(testAction);
	}

	// This will create the columns for the table
	private void createColumns(final Composite parent, final TableViewer viewer) {
		String[] titles = { "", "Label", "JDBC Url", "User", "Password", "Status", "Errors" };
		int[] bounds = { 100, 0, 1, 0, 0, 0, 0 };

		TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0], 0);
		col.setLabelProvider(new DrawLabelProvider());

		col = createTableViewerColumn(titles[1], bounds[1], 1);
		col.setEditingSupport(new LabelEditingSupport(getViewer()));
		col.setLabelProvider(new LabelLabelProvider(null));

		col = createTableViewerColumn(titles[2], bounds[2], 2);
		col.setEditingSupport(new UrlEditingSupport(getViewer()));
		col.setLabelProvider(new UrlLabelProvider(null));

		col = createTableViewerColumn(titles[3], bounds[3], 3);
		col.setEditingSupport(new UsernameEditingSupport(getViewer()));
		col.setLabelProvider(new UsernameLabelProvider(null));

		col = createTableViewerColumn(titles[4], bounds[4], 4);
		col.setEditingSupport(new PasswordEditingSupport(getViewer()));
		col.setLabelProvider(new PasswordLabelProvider(null));

		col = createTableViewerColumn(titles[5], bounds[5], 5);
		col.setLabelProvider(new StatusLabelProvider(null));

		col = createTableViewerColumn(titles[6], bounds[6], 6);
		col.setLabelProvider(new ErrorLabelProvider(getSite().getShell().getDisplay().getSystemColor(SWT.COLOR_RED)));

	}

	private TableViewerColumn createTableViewerColumn(String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(viewer, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		column.addSelectionListener(getSelectionAdapter(column, colNumber));
		return viewerColumn;

	}

	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
				int dir = comparator.getDirection();
				viewer.getTable().setSortDirection(dir);
				viewer.getTable().setSortColumn(column);
				viewer.refresh();
			}
		};
		return selectionAdapter;
	}

	public void searchDatabaseConfigs() {
		getViewer().refresh();
		DeploymentDescriptorHandler ddh = new DeploymentDescriptorHandler();
		try {
			
			DeploymentDescriptorDialog ddDialog = new DeploymentDescriptorDialog(getSite().getShell(),
					ddh.getSearchDescriptors());

			if (ddDialog.open() == Window.OK) {
				ModelProvider.INSTANCE.getConnectionItems().addAll(ddDialog.getSelectedItems());	
			}
			
			getViewer().refresh();
		} catch (Exception e) {
			showError(e.getMessage());
		}
	}

	public void duplicate() {
		ConnectionItem selItem = (ConnectionItem) getSelection().getFirstElement();
		ModelProvider.INSTANCE.duplicate(selItem);
		getViewer().refresh();
	}

	public void add() {
		ConnectionItem item = new ConnectionItem(UUID.randomUUID().toString(), "display name", "JDBC url", "user",
				"password", "", true);
		ModelProvider.INSTANCE.getConnectionItems().add(item);
		getViewer().refresh();

	}

	public void delete() {

		@SuppressWarnings("unchecked")
		List<ConnectionItem> sel = getSelection().toList();
		for (ConnectionItem item : sel) {
			ModelProvider.INSTANCE.delete(item);
		}
		getViewer().refresh();
	}

	public void list() {
		ModelProvider.INSTANCE.saveModel();
		Job job = new Job("DbCompare (Listing) running...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Iterator<?> it = getSelection().iterator();
				ConnectionItem p1 = (ConnectionItem) it.next();

				ModelProvider.INSTANCE.setErrorMessage(p1.getId(), "", true);
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						viewer.refresh();
					}
				});

				listDatabase(p1.getId(), p1.getUrl(), p1.getUsername(), p1.getPassword());
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();

	}

	public void compare() {
		ModelProvider.INSTANCE.saveModel();
		Job job = new Job("DbCompare running...") {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				Iterator<?> it = getSelection().iterator();
				ConnectionItem p1 = (ConnectionItem) it.next();
				ConnectionItem p2 = (ConnectionItem) it.next();

				ModelProvider.INSTANCE.setErrorMessage(p1.getId(), "", true);
				ModelProvider.INSTANCE.setErrorMessage(p2.getId(), "", true);
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						viewer.refresh();
					}
				});

				compareDatabases(p1.getId(), p1.getUrl(), p1.getUsername(), p1.getPassword(), p2.getId(), p2.getUrl(),
						p2.getUsername(), p2.getPassword());
				return Status.OK_STATUS;
			}

		};
		job.setUser(true);
		job.schedule();

	}

	private void listDatabase(String idL, String urlL, String userL, String pwL) {
		boolean noConnectionError = true;

		try {
			DatabaseStructureReader rds = new DatabaseStructureReaderFactory().getDatabaseStructureReader(getSite().getShell(), urlL);
			sbLeft = rds.readDbStructure(urlL, userL, pwL);

		} catch (CustomSQLException e1) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (e1.getErrorMessage() != null) {
						ModelProvider.INSTANCE.setErrorMessage(idL, e1.getErrorMessage(), false);
						viewer.refresh();

					}

				}
			});
			noConnectionError = false;
		}

		if (!noConnectionError) {
			return;
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {

				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = window.getActivePage();
				IStorage storage = new StringStorage(sbLeft.toString());
				IStorageEditorInput input = new StringInput(storage);

				try {
					page.openEditor(input, "org.eclipse.ui.DefaultTextEditor");
				} catch (PartInitException e) {
					e.printStackTrace();
				}

			}
		});
	}

	private void compareDatabases(String idL, String urlL, String userL, String pwL, String idR, String urlR,
			String userR, String pwR) {
		boolean noConnectionErrorLeft = true;
		boolean noConnectionErrorRight = true;

		try {
			DatabaseStructureReader rds = new DatabaseStructureReaderFactory().getDatabaseStructureReader(getSite().getShell(), urlL);
			sbLeft = rds.readDbStructure(urlL, userL, pwL);

		} catch (CustomSQLException e1) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					if (e1.getErrorMessage() != null) {
						ModelProvider.INSTANCE.setErrorMessage(idL, e1.getErrorMessage(), false);
						viewer.refresh();

					}

				}
			});
			noConnectionErrorLeft = false;
		}
		try {
			DatabaseStructureReader rdsRight = new DatabaseStructureReaderFactory().getDatabaseStructureReader(getSite().getShell(), urlR);
			sbRight = rdsRight.readDbStructure(urlR, userR, pwR);
		} catch (CustomSQLException e1) {
			Display.getDefault().asyncExec(new Runnable() {

				public void run() {
					if (e1.getErrorMessage() != null) {
						ModelProvider.INSTANCE.setErrorMessage(idR, e1.getErrorMessage(), false);
						viewer.refresh();

					}
				}
			});
			noConnectionErrorRight = false;
		}

		if (!noConnectionErrorLeft || !noConnectionErrorRight) {
			return;
		}

		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				CompareEditorAction compareAction = new CompareEditorAction(sbLeft.toString(), sbRight.toString());
				compareAction.run(SqlServerCopy.this.compareAction);
			}
		});
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}

	@Override
	public void dispose() {
		ModelProvider.INSTANCE.saveModel();
	}

	public IStructuredSelection getSelection() {
		return selection;
	}

	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}

	protected void showError(String message) {

		MessageDialog.openError(getSite().getShell(), "DbCompare Error: " + getTitle(), message);
	}

}
