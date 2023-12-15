package ch.ludin.dbcompare.view;

import org.eclipse.swt.graphics.Image;

import ch.ludin.dbcompare.Activator;
import ch.ludin.dbcompare.model.ConnectionItem;

public class DrawLabelProvider extends CentredImageCellLabelProvider {
	private Image logoPostgres;
	private Image logoSqlserver;

	public DrawLabelProvider() {
		super();
		logoPostgres = Activator.getDefault().getRegistry().get(Activator.IMAGE_ID_LOGO_POSTGRES);
		logoSqlserver = Activator.getDefault().getRegistry().get(Activator.IMAGE_ID_LOGO_SQLSERVER);
	}

	@Override
	protected Image getImage(Object element) {
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


}
