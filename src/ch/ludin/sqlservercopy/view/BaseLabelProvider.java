package ch.ludin.sqlservercopy.view;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.IFontProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import ch.ludin.sqlservercopy.model.ConnectionItem;

public class BaseLabelProvider extends ColumnLabelProvider implements IColorProvider, IFontProvider {
	Color fgColor = null;
	Font font = JFaceResources.getFont(JFaceResources.TEXT_FONT);
	
	public BaseLabelProvider(Color fgColor) {
		super();
		this.fgColor = fgColor;
	}


	@Override
	public Font getFont(Object element) {
		return font;
	}


	@Override
    public Color getBackground(Object element) {
        return null;
    }

    @Override
    public Color getForeground(Object element) {
        return this.fgColor;
    }

    @Override
    public String getText(Object element) {
        return null;
    }

}
