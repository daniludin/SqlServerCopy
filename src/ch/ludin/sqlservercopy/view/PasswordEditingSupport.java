package ch.ludin.sqlservercopy.view;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;

import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.model.ModelProvider;

public class PasswordEditingSupport  extends BaseEditingSupport {


    public PasswordEditingSupport(TableViewer viewer) {
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
        return ((ConnectionItem) element).getPassword();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
        ((ConnectionItem) element).setPassword(String.valueOf(userInputValue));
        getViewer().update(element, null);
        ModelProvider.INSTANCE.saveModel();
    }
}