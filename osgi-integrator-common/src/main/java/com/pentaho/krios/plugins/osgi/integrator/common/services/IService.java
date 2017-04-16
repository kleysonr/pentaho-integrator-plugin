package com.pentaho.krios.plugins.osgi.integrator.common.services;

import java.util.Map;

/*
 * Methods that a integrator plugin MUST provide.
 */

public interface IService {
	
	/*
	 * Return the list of the configurable plugin parameters
	 */
	public Map<String, Object> getProps();

	/*
	 * Return the configuration pid
	 */
	public String getConfigPid();
	
	/*
	 * Is the plugin active ?
	 */
	public boolean isActive();
	
	/*
	 * Plugin name
	 */
	public String pluginName();
	
	/*
	 * Plugin version
	 */
	public String pluginVersion();
	
	/*
	 * Plugin vendor/author
	 */
	public String pluginVendor();
	
	/*
	 * Plugin description
	 */
	public String pluginDescription();
	
	/*
	 * Plugin url/documentation
	 */
	public String pluginUrl();

}
