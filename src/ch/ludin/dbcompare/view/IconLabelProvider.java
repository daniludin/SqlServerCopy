package ch.ludin.dbcompare.view;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import ch.ludin.dbcompare.Activator;
import ch.ludin.dbcompare.model.ConnectionItem;

public class IconLabelProvider extends BaseLabelProvider {
	Color fgColor = null;
	Font font = JFaceResources.getFont(JFaceResources.TEXT_FONT);
	private Image logoPostgres;
	private Image logoSqlserver;
	
	public IconLabelProvider(Color fgColor) {
		super(fgColor);
		logoPostgres = Activator.getDefault().getRegistry().get(Activator.IMAGE_ID_LOGO_POSTGRES);
		logoSqlserver = Activator.getDefault().getRegistry().get(Activator.IMAGE_ID_LOGO_SQLSERVER);
	}


	@Override
	public Image getImage(Object element) {
        ConnectionItem p = (ConnectionItem) element;
        if (p.getUrl().contains("postgres")) {
        	return logoPostgres;
		}
        if (p.getUrl().contains("sqlserver")) {
        	return logoSqlserver;
		}
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
        return null;
    }

}
