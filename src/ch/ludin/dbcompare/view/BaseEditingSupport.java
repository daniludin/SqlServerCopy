package ch.ludin.dbcompare.view;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Text;

import ch.ludin.dbcompare.model.ConnectionItem;

public class BaseEditingSupport  extends EditingSupport {

    private final TableViewer viewer;
    private final CellEditor editor;

    public BaseEditingSupport(TableViewer viewer) {
        super(viewer);
        this.viewer = viewer;
        this.editor = new TextCellEditor(viewer.getTable());
        ((Text) this.editor.getControl()).setFont(JFaceResources.getFont(JFaceResources.TEXT_FONT));;
   
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        return ((ConnectionItem) element).toString();
    }

    @Override
    protected void setValue(Object element, Object userInputValue) {
    }
}