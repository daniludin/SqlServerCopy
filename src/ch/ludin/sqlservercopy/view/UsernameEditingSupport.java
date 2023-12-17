package ch.ludin.sqlservercopy.view;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;

import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.model.ModelProvider;

public class UsernameEditingSupport  extends BaseEditingSupport {

    public UsernameEditingSupport(TableViewer viewer) {
        super(viewer);
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return super.getCellEditor(element);
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        return ((ConnectionItem) element).getUsername();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
        ((ConnectionItem) element).setUsername(String.valueOf(userInputValue));
        getViewer().update(element, null);
        ModelProvider.INSTANCE.saveModel();
        
//        List<ConnectionItem> connectionItems = ModelProvider.INSTANCE.getConnectionItems();
//        for (ConnectionItem item : connectionItems) {
//			System.out.println("-------------------------");
//			System.out.println(item.getId());
//			System.out.println(item.getLabel());
//			System.out.println(item.getUrl());
//			System.out.println(item.getUsername());
//			System.out.println(item.getPassword());
//			System.out.println(item.getDriverClassName());
//		}
    }
}