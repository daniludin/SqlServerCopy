package ch.ludin.dbcompare.view;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import ch.ludin.dbcompare.model.ConnectionItem;

public class LabelLabelProvider extends BaseLabelProvider {
	Color fgColor = null;
	Font font = JFaceResources.getFont(JFaceResources.TEXT_FONT);

	public LabelLabelProvider(Color fgColor) {
		super(fgColor);
	}


	@Override
	public Image getImage(Object element) {
        return null;        
	}


	@Override
	public void dispose() {
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
        ConnectionItem p = (ConnectionItem) element;
        return p.getLabel();
    }

}
