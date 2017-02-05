package com.pentaho.krios.plugins.osgi.integrator.manager.services;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/*
 * Plugins to provide a new Token provider must implement
 * this interface.
 */

public interface ITokenService extends IService {
	
	/*
	 * Use the URL params to validate the request. If parameters are validated 
	 * return the username to be authenticated, otherwise return null.
	 */
	public String authenticate(HashMap<String, String[]> params);
	
}
