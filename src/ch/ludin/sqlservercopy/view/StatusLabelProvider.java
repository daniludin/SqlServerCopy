package ch.ludin.sqlservercopy.view;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

import ch.ludin.sqlservercopy.Activator;
import ch.ludin.sqlservercopy.model.ConnectionItem;

public class StatusLabelProvider extends BaseLabelProvider {

	private Image ERROR;

	public StatusLabelProvider(Color fgColor) {
		super(fgColor);
		ERROR = Activator.getDefault().getRegistry().get(Activator.IMAGE_ID_ERROR);
	}

	@Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
	public Image getImage(Object element) {
		ConnectionItem item = (ConnectionItem) element;
		if (item.isConnectionStatus()) {
			return null;
		}
		return ERROR;
	}
	@Override
	public void dispose() {
	}

	@Override
    public Color getForeground(Object element) {
        return null;
    }

    @Override
    public String getText(Object element) {
    	return null;
    }

}
