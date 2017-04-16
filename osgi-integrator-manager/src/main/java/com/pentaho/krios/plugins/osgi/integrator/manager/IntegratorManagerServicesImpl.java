package com.pentaho.krios.plugins.osgi.integrator.manager;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.osgi.service.cm.ConfigurationAdmin;

import com.pentaho.krios.plugins.osgi.integrator.common.services.IService;
import com.pentaho.krios.plugins.osgi.integrator.common.services.ITokenService;
import com.pentaho.krios.plugins.osgi.integrator.common.services.IntegratorManagerService;

public class IntegratorManagerServicesImpl implements IntegratorManagerService {
	
	// OSGi "Persistent ID" of this bundle
	// Must match with persistent-id tag in the blueprint.xml file
	static private String CONFIG_PID = "com.pentaho.krios.plugins.osgi.integrator.manager";
	
	static private boolean active;
	
	// Configurating Admin Service
	static private ConfigurationAdmin configAdmin;
	
	// Maintain a list of all the token providers
	static private List<ITokenService> tokenProviders;

	/*
	 * Return the list of token providers
	 */
	public static List<ITokenService> getTokenProviders() {
		return tokenProviders;
	}

	/*
	 * getPluginPropsList() returns the list of the configurable properties for the requested plugin.
	 */
	public static Object getPluginPropsList(IService provider) throws IOException 
	{
		return provider.getProps();
	}

	/*
	 * getConfigFileLocation() returns the full path of the configuration file for the requested plugin.
	 */
	public static String getConfigFileLocation(String configPid) throws IOException
	{
		return configAdmin.getConfiguration(configPid).getProperties().get("felix.fileinstall.filename").toString();
	}
	
	/*
	 * setTokenProviders() called every time that a new plugin start/stop. It updates the list of providers.
	 * Method called directly by the OSGI manager.
	 */
	@SuppressWarnings("static-access")
	public void setTokenProviders(List<ITokenService> tokenProviders) {
		this.tokenProviders = tokenProviders;
	}

	/*
	 * Injected by blueprint - Configuration Admin Service
	 */
	public void setConfigAdmin(ConfigurationAdmin configAdmin) {
		this.configAdmin = configAdmin;
	}

	/*
	 * pluginName() returns the name of the plugin.
	 * This method is specified in the IService interface.
	 */
	public String pluginName() {
		return Activator.getContext().getBundle().getHeaders().get("Bundle-Name");
	}

	/*
	 * pluginVersion() returns the version of the plugin.
	 * This method is specified in the IService interface.
	 */
	public String pluginVersion() {
		return Activator.getContext().getBundle().getHeaders().get("Bundle-Version");
	}

	/*
	 * pluginVendor() returns the vendor or author of the plugin.
	 * This method is specified in the IService interface.
	 */
	public String pluginVendor() {
		return Activator.getContext().getBundle().getHeaders().get("Bundle-Vendor");
	}

	/*
	 * pluginDescription() returns an about of the plugin.
	 * This method is specified in the IService interface.
	 */
	public String pluginDescription() {
		return Activator.getContext().getBundle().getHeaders().get("Bundle-Description");
	}

	/*
	 * pluginUrl() returns the url documentation of the plugin.
	 * This method is specified in the IService interface.
	 */
	public String pluginUrl() {
		return Activator.getContext().getBundle().getHeaders().get("Bundle-DocURL");
	}

	/*
	 * getProps() returns the map of all configurable parameters and their active values for the plugin.
	 * This method is specified in the IService interface.
	 */
	public Map<String, Object> getProps() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * getConfigPid() returns the unique PID configuration that is registered for the plugin.
	 * This method is specified in the IService interface.
	 */
	public String getConfigPid() {
		return CONFIG_PID;
	}

	/*
	 * isActive() returns if plugin is active or not.
	 * This method is specified in the IService interface.
	 */
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
		
		if (active)
			Activator.getSecurityHelper().enableFilter();
		else
			Activator.getSecurityHelper().disableFilter();
	}

}
