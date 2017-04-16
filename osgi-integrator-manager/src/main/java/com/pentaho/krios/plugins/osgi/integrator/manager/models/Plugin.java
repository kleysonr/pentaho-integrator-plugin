package com.pentaho.krios.plugins.osgi.integrator.manager.models;

import java.util.Map;

/*
 * Output template for the RestAdmin API
 */

public class Plugin {
	
	/*
	 * Return the list of the configurable plugin parameters
	 */
	private Map<String, Object> props;

	/*
	 * Configuration file
	 */
	private String configFile;
	
	/*
	 * Is the plugin active ?
	 */
	private boolean active;
	
	/*
	 * Plugin name
	 */
	private String name;
	
	/*
	 * Plugin version
	 */
	private String version;
	
	/*
	 * Plugin vendor/author
	 */
	private String vendor;
	
	/*
	 * Plugin description
	 */
	private String description;
	
	/*
	 * Plugin url/documentation
	 */
	private String url;

	public Map<String, Object> getProps() {
		return props;
	}

	@SuppressWarnings("unchecked")
	public void setProps(Object object) {
		this.props = (Map<String, Object>) object;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}	

}
