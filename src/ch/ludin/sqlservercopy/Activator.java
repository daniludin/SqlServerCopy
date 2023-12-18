package ch.ludin.sqlservercopy;

import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "SqlServerCopy"; //$NON-NLS-1$
	public static final String IMAGE_ID_ADD = "add";
	public static final String IMAGE_ID_CHECKED = "checked";
	public static final String IMAGE_ID_UNCHECKED = "unchecked";
	public static final String IMAGE_ID_ERROR = "alert_obj";
	public static final String IMAGE_ID_LOGO_POSTGRES = "logo";
	public static final String IMAGE_ID_LOGO_SQLSERVER = "sqlserver_logo";
	public static final String IMAGE_ID_TWO_WAY_COMPARE = "twowaycompare";
	public static final String IMAGE_ID_DELETE = "delete";
	public static final String IMAGE_ID_DUPLICATE = "duplicate";
	public static final String IMAGE_ID_LIST = "list";
	public static final String IMAGE_ID_COPY = "copy";
	public static final String IMAGE_ID_LOAD = "loading4";


	private ImageRegistry registry;
	// more code...

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	public static ImageDescriptor getImageDescriptor(String file) {
		// assume that the current class is called View.java
		Bundle bundle = FrameworkUtil.getBundle(SqlServerCopy.class);
		URL url = FileLocator.find(bundle, new Path("icons/" + file), null);
		return ImageDescriptor.createFromURL(url);
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;

		Bundle bundle = Platform.getBundle(PLUGIN_ID);
		setRegistry(new ImageRegistry());

		registry.put(IMAGE_ID_ADD, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/add.gif"), null)));
		registry.put(IMAGE_ID_CHECKED, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/checked.gif"), null)));
		registry.put(IMAGE_ID_UNCHECKED, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/unchecked.gif"), null)));
		registry.put(IMAGE_ID_ERROR, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/alert_obj.png"), null)));
		registry.put(IMAGE_ID_LOGO_POSTGRES, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/logo.png"), null)));
		registry.put(IMAGE_ID_LOGO_SQLSERVER, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/sqlserver_logo.png"), null)));
		registry.put(IMAGE_ID_TWO_WAY_COMPARE, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/twowaycompare.gif"), null)));
		registry.put(IMAGE_ID_DELETE, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/delete.gif"), null)));
		registry.put(IMAGE_ID_DUPLICATE, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/duplicate.png"), null)));
		registry.put(IMAGE_ID_LIST, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/list.gif"), null)));
		registry.put(IMAGE_ID_COPY, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/copyWithTag.gif"), null)));
		registry.put(IMAGE_ID_LOAD, ImageDescriptor.createFromURL(FileLocator.find(bundle, new Path("icons/loading4.gif"), null)));
		
		

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@SuppressWarnings("deprecation")
	public static ImageDescriptor iconNamed(String name) {

		String path = "icons/" + name;
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public ImageRegistry getRegistry() {
		return registry;
	}

	public void setRegistry(ImageRegistry registry) {
		this.registry = registry;
	}

}
