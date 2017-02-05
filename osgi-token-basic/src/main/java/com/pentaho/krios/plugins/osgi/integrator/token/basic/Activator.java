package com.pentaho.krios.plugins.osgi.integrator.token.basic;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {
	
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
	}
	
	public static BundleContext getContext()
	{
		return bc;
	}
}
