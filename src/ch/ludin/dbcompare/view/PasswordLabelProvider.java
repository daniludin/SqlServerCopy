package ch.ludin.dbcompare.view;

import org.eclipse.swt.graphics.Color;

import ch.ludin.dbcompare.model.ConnectionItem;

public class PasswordLabelProvider extends BaseLabelProvider {

	public PasswordLabelProvider(Color fgColor) {
		super(fgColor);
	}

	@Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        return super.getForeground(element);
    }

    @Override
    public String getText(Object element) {
        ConnectionItem p = (ConnectionItem) element;
        return p.getPassword().replaceAll(".", "*");
    }

}
