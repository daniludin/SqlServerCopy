package ch.ludin.dbcompare.handler;

import org.eclipse.jface.viewers.ArrayContentProvider;

public class DialogContentProvider extends ArrayContentProvider {

	@Override
	public Object[] getElements(Object inputElement) {
		 Object[] elements = super.getElements(inputElement);
		 
//		 Object[] newElements = new Object[2];
//			newElements[0] = elements[1];
//			newElements[1] = elements[2];
//		
//			return newElements;
			return elements;
	}

}
