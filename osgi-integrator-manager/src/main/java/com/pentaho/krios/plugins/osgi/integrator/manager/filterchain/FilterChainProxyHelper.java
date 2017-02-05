package com.pentaho.krios.plugins.osgi.integrator.manager.filterchain;

import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.firewall.DefaultHttpFirewall;

public class FilterChainProxyHelper {
	
	private FilterChainProxy defaultFilterChainProxy;

	public FilterChainProxyHelper() 
	{
		init();
	}
	
	/**
	 * Init method
	 */
	private void init() 
	{
		// This method gets the default filterChainProxy bean from PentahoSystem
		defaultFilterChainProxy = PentahoSystem.get( FilterChainProxy.class, "filterChainProxy", PentahoSessionHolder.getSession() );
		
		//enableCustomHttpFirewall();
	}
	
	/**
	 * Set a new HttpFirewall that will work as a proxy for the new FilterChainProxy
	 */
	public void enableCustomHttpFirewall() 
	{
		CustomHttpFirewall customHttpFirewall = new CustomHttpFirewall();
		defaultFilterChainProxy.setFirewall(customHttpFirewall);	
	}
	
	/**
	 * Set a new HttpFirewall that will work as a proxy for the new FilterChainProxy
	 */
	public void disableCustomHttpFirewall() 
	{
		defaultFilterChainProxy.setFirewall( new DefaultHttpFirewall() );	
	}

}
