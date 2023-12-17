package ch.ludin.sqlservercopy.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.JavaCore;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import ch.ludin.sqlservercopy.model.ConnectionItem;
import ch.ludin.sqlservercopy.model.DescriptorItem;

public class DeploymentDescriptorHandler {


	public List<ConnectionItem> getSearchDescriptors() throws Exception {
		List<ConnectionItem> result = new ArrayList<ConnectionItem>();

		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();

		IProject[] projects = workspaceRoot.getProjects();
		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.isOpen() && project.hasNature(JavaCore.NATURE_ID)) {
				String projectName = project.getName();
				
				for (DescriptorItem.FolderKey key : DescriptorItem.FolderKey.values()) {
					IFolder descrFolder = project.getFolder(key.getFolderKey());
					if (!descrFolder.exists()) {
						continue;
					}
					for (int k = 0; k < descrFolder.members().length; k++) {
						IResource iResource = descrFolder.members()[k];
						IFile file = (IFile) iResource;
						if (!file.getName().endsWith(".xml")) {
							continue;
						}

						try {
							DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
							DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
							Document doc = docBuilder.parse(file.getContents());
							XPathFactory xpf = XPathFactory.newInstance();
							XPath xpath = xpf.newXPath();
							Element userElement = (Element) xpath.evaluate("/Context/Resource", doc, XPathConstants.NODE);
														
							// @formatter:off
							ConnectionItem item = new ConnectionItem(
									UUID.randomUUID().toString(), 
									projectName + " " + key.name().toUpperCase().replaceAll("[0-9]", ""),
									userElement.getAttribute("url"), 
									userElement.getAttribute("username"), 
									userElement.getAttribute("password"), 
									userElement.getAttribute("driverClassName"), 
									true);
							// @formatter:on
							result.add(item);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} 
				}
				
				
			}
		}
		return result;
	}


}
