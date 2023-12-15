package ch.ludin.dbcompare.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ConnectionItem {
	private String id;
	private String label;
	String url;
	String username;
	String password;
	private String driverClassName;
	
	private String connectionError;
	boolean connectionStatus;
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public ConnectionItem(String id, String label, String url, String username, String password, String driverClassName, boolean connectionStatus) {
		super();
		this.id = id;
		this.label = label;
		this.url = url;
		this.username = username;
		this.password = password;
		this.driverClassName = driverClassName;
		this.connectionStatus = connectionStatus;
	}
    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(propertyName, listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
		//propertyChangeSupport.firePropertyChange("id", this.id, this.id = id);
	}

	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
		//propertyChangeSupport.firePropertyChange("url", this.url, this.url = url);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		//propertyChangeSupport.firePropertyChange("username", this.username, this.username = username);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		//propertyChangeSupport.firePropertyChange("password", this.password, this.password = password);
	}
	public boolean isConnectionStatus() {
		return connectionStatus;
	}
	public void setConnectionStatus(boolean connectionStatus) {
		this.connectionStatus = connectionStatus;
	}
	public String getConnectionError() {
		return connectionError;
	}
	
	public void setConnectionError(String connectionError) {
		this.connectionError = connectionError;
	}

	@Override
	public String toString() {
		StringBuffer result = new StringBuffer();
		result.append(getId());
		result.append("\t");
		result.append(getLabel());
		result.append("\t");
		result.append(getUrl());
		result.append("\t");
		result.append(getUsername());
		result.append("\t");
		result.append(getPassword());
		result.append("\t");
		result.append(isConnectionStatus());
		return result.toString();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
		//propertyChangeSupport.firePropertyChange("label", this.label, this.label = label);
	}

	public String getDriverClassName() {
		return driverClassName;
	}

	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
		//propertyChangeSupport.firePropertyChange("driverClassName", this.driverClassName, this.driverClassName = driverClassName);
	}


	
}
