package ch.ludin.sqlservercopy.view;

import org.eclipse.swt.graphics.Color;

import ch.ludin.sqlservercopy.model.ConnectionItem;

public class UsernameLabelProvider extends BaseLabelProvider {

	public UsernameLabelProvider(Color fgColor) {
		super(fgColor);
	}

	@Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
        ConnectionItem p = (ConnectionItem) element;
        return p.getUsername();
    }

}
