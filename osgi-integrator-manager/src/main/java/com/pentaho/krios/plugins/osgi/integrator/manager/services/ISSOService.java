package com.pentaho.krios.plugins.osgi.integrator.manager.services;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*
 * Plugins to provide a new SSO provider must implement
 * this interface.
 */

public interface ISSOService extends IService {
	
	/*
	 * Authenticate the request over the SSO implementation.
	 */
	public String authenticate(ServletRequest request, ServletResponse response);
	
}
