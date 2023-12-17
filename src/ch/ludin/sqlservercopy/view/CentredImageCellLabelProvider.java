package ch.ludin.sqlservercopy.view;

import org.eclipse.jface.viewers.OwnerDrawLabelProvider;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.TableItem;

public abstract class CentredImageCellLabelProvider extends OwnerDrawLabelProvider {
	public CentredImageCellLabelProvider() {
		super();
	}

	@Override
	protected void measure(Event event, Object element) {
		// No action
	}

	@Override
	protected void erase(Event event, Object element) {
		// Don't call super.erase() to suppress non-standard selection draw
	}

	@Override
	protected void paint(Event event, Object element) {
		TableItem item = (TableItem) event.item;

		Rectangle itemBounds = item.getBounds(event.index);

		GC gc = event.gc;

		Image image = getImage(element);
		if (image == null) {
			return;
		}
		Rectangle imageBounds = image.getBounds();

		int x = event.x + Math.max(0, (itemBounds.width - imageBounds.width) / 2);
		int y = event.y + Math.max(0, (itemBounds.height - imageBounds.height) / 2);

		gc.drawImage(image, x, y);
	}

	protected abstract Image getImage(Object element);
}
