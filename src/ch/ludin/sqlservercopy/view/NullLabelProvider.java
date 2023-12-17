package ch.ludin.sqlservercopy.view;

import org.eclipse.swt.graphics.Color;

public class NullLabelProvider extends BaseLabelProvider {

	public NullLabelProvider(Color fgColor) {
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
    	return null;
    }

}
