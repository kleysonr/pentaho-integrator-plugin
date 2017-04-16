package com.pentaho.krios.plugins.osgi.integrator.manager;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.pentaho.platform.engine.security.SecurityHelper;

import com.pentaho.krios.plugins.osgi.integrator.manager.helpers.FilterChainProxyHelper;

public class Activator implements BundleActivator {
	
	// Control the changes on the filterChainProxy
	private static FilterChainProxyHelper securityHelper = new FilterChainProxyHelper();
	
	private static BundleContext bc;
	
	/*
	 * start() method gets called when running osgi:start from the console
	 */
	public void start(BundleContext context) throws Exception 
	{
		System.out.println("Starting " + context.getBundle().getHeaders().get("Bundle-Description") + "  Version: "+ context.getBundle().getHeaders().get("Bundle-Version"));
		bc = context;
	}

	/*
	 * stop() method gets called when running osgi:stop from the console
	 */
	public void stop(BundleContext context) throws Exception 
	{
		System.out.println("Stopping " + context.getBundle().getHeaders().get("Bundle-Description") + "  Version: "+ context.getBundle().getHeaders().get("Bundle-Version"));
		securityHelper.disableFilter();
	}
	
	public static BundleContext getContext()
	{
		return bc;
	}	
	
	public static FilterChainProxyHelper getSecurityHelper()
	{
		return securityHelper;
	}

}
