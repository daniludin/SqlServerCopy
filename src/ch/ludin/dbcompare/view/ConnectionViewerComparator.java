package ch.ludin.dbcompare.view;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import ch.ludin.dbcompare.model.ConnectionItem;

public class ConnectionViewerComparator extends ViewerComparator {
    private int propertyIndex;
    private static final int DESCENDING = 1;
    private int direction = DESCENDING;

    public ConnectionViewerComparator() {
        this.propertyIndex = 0;
        direction = DESCENDING;
    }

    public int getDirection() {
        return direction == 1 ? SWT.DOWN : SWT.UP;
    }

    public void setColumn(int column) {
        if (column == this.propertyIndex) {
            // Same column as last sort; toggle the direction
            direction = 1 - direction;
        } else {
            // New column; do an ascending sort
            this.propertyIndex = column;
            direction = DESCENDING;
        }
    }

    @Override
    public int compare(Viewer viewer, Object e1, Object e2) {
        ConnectionItem p1 = (ConnectionItem) e1;
        ConnectionItem p2 = (ConnectionItem) e2;
        int rc = 0;
        switch (propertyIndex) {
        case 0:
            break;
        case 1:
            rc = p1.getLabel().compareTo(p2.getLabel());
            break;
        case 2:
            rc = p1.getUrl().compareTo(p2.getUrl());
            break;
        case 3:
            rc = p1.getUsername().compareTo(p2.getUsername());
            break;
        case 4:
            rc = p1.getPassword().compareTo(p2.getPassword());
            break;
        default:
            rc = 0;
        }
        // If descending order, flip the direction
        if (direction == DESCENDING) {
            rc = -rc;
        }
        return rc;
    }

}
