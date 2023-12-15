package ch.ludin.dbcompare.view;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.swt.graphics.Color;

import ch.ludin.dbcompare.model.ConnectionItem;

public class ColorColumnLabelProvider extends ColumnLabelProvider implements IColorProvider {
	//final static Color colorBg = new Color(Display.getDefault(), 239, 239, 239);
    Color colorBg;
	
	
	public ColorColumnLabelProvider(Color colorBg) {
		super();
		this.colorBg = colorBg;
	}

	@Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        return colorBg;
    }

    @Override
    public String getText(Object element) {
        ConnectionItem p = (ConnectionItem) element;
        return p.getPassword();
    }

}
