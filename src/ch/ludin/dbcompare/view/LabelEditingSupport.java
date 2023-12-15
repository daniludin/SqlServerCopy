package ch.ludin.dbcompare.view;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TableViewer;

import ch.ludin.dbcompare.model.ConnectionItem;
import ch.ludin.dbcompare.model.ModelProvider;

public class LabelEditingSupport extends BaseEditingSupport {

	public LabelEditingSupport(TableViewer viewer) {
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
		return ((ConnectionItem) element).getLabel();
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		((ConnectionItem) element).setLabel(String.valueOf(userInputValue));
		getViewer().update(element, null);
		ModelProvider.INSTANCE.saveModel();
	}
}