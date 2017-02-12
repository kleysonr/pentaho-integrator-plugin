package com.pentaho.krios.plugins.osgi.integrator.token.basic;

import java.util.HashMap;
import java.util.Map;

import com.pentaho.krios.plugins.osgi.integrator.common.services.ITokenService;

public class TokenServiceImpl implements ITokenService {
	
	// OSGi "Persistent ID" of this bundle
	// Must match with persistent-id tag in the blueprint.xml file
	static String CONFIG_PID = "com.pentaho.krios.plugins.osgi.integrator.token.basic";
	
	private final Map<String,Object> configurableProperties = new HashMap<String, Object>();
	
	private String principalName;
	private String token;
	private String userQueryParam;
	private String tokenQueryParam;
	
	private boolean active;
	
	public TokenServiceImpl() 
	{
		System.out.println( "Loading: " + this.pluginDescription() );
	}		
	
	/*
	 * authenticate() is called to validate the params. If params are valid return
	 * the username to be authenticates, otherwise return null.
	 * This method is specified in the ITokenService interface.
	 */
	public String authenticate(HashMap<String, String[]> params) 
	{ 
		if ( principalName.equalsIgnoreCase(params.get(userQueryParam)[0]) && token.equalsIgnoreCase(params.get(tokenQueryParam)[0]) ) 
		{
			return principalName;
		} 
		else
		{
			return null;
		}
		
	}

	/*
	 * getProps() returns the map of all configurable parameters and their active values for the plugin.
	 * This method is specified in the IService interface.
	 */
	public Map<String, Object> getProps() 
	{
		return configurableProperties;
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

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
		this.configurableProperties.put("principalName", principalName);
	}

	public void setToken(String token) {
		this.token = token;
		this.configurableProperties.put("token", token);
	}

	public void setUserQueryParam(String userQueryParam) {
		this.userQueryParam = userQueryParam;
		this.configurableProperties.put("userQueryParam", userQueryParam);
	}

	public void setTokenQueryParam(String tokenQueryParam) {
		this.tokenQueryParam = tokenQueryParam;
		this.configurableProperties.put("tokenQueryParam", tokenQueryParam);
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
