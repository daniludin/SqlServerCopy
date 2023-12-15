package ch.ludin.dbcompare.view;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import ch.ludin.dbcompare.Activator;
import ch.ludin.dbcompare.model.ConnectionItem;

public class ConnectionsLabelProvider implements ITableLabelProvider{
    private static final Image CHECKED =  Activator.iconNamed("ball.red.gif").createImage();    
    
	@Override
	public void addListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		CHECKED.dispose();
		
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		Image image = null;
		ConnectionItem item = (ConnectionItem) arg0;
		switch (arg1) {
		case 4:
			if (!item.isConnectionStatus()) {
				image = CHECKED;
			}
			
			break;
		}
		return image;
	}

	@Override
	public String getColumnText(Object arg0, int arg1) {
		ConnectionItem item = (ConnectionItem) arg0;
		String text = "";
		switch (arg1) {
		case 0:
			text = item.getLabel();
			break;
		case 1:
			text = item.getUrl();
			break;
		case 2:
			text = item.getUsername();
			break;
		case 3:
			text = item.getPassword().replaceAll(".", "*");
			break;
		case 4:
			//text = item.isConnectionStatus() ? "ja" : "nein";
			text = "";
			break;
		case 5:
			text = item.getConnectionError();
			break;
		}
		return text;
	}

}
