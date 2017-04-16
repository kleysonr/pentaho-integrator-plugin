package com.pentaho.krios.plugins.osgi.integrator.manager.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.Filter;

import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;

import com.pentaho.krios.plugins.osgi.integrator.manager.filter.TokenAuthenticationFilter;

public class FilterChainProxyHelper {
	
	private FilterChainProxy proxy;

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
		proxy = PentahoSystem.get( FilterChainProxy.class, "filterChainProxy", PentahoSessionHolder.getSession() );
	}
	
	/**
	 * Enable the filter
	 */
	public void enableFilter() 
	{
		List<SecurityFilterChain> list = proxy.getFilterChains();
		Iterator<SecurityFilterChain> it = list.iterator();
		
		TokenAuthenticationFilter tokenAuthenticationFilter = new TokenAuthenticationFilter();		
		
		while(it.hasNext()){
			DefaultSecurityFilterChain fc = (DefaultSecurityFilterChain) it.next();
			if( (fc.getRequestMatcher() instanceof AntPathRequestMatcher) || (fc.getRequestMatcher() instanceof AnyRequestMatcher) ) {
				ArrayList<Filter> filters = (ArrayList<Filter>) fc.getFilters();
				
				int index = 0;
				for (Filter filter: filters) {
				    if (filter instanceof AnonymousAuthenticationFilter) {           
				        break;
				    }
				    index++;
				}				
				
				filters.add(index, tokenAuthenticationFilter);
			}
		}
	}
	
	/**
	 * Disable Filter
	 */
	public void disableFilter() 
	{
		List<SecurityFilterChain> list = proxy.getFilterChains();
		Iterator<SecurityFilterChain> it = list.iterator();
		
		while(it.hasNext()){
			DefaultSecurityFilterChain fc = (DefaultSecurityFilterChain) it.next();
			if( (fc.getRequestMatcher() instanceof AntPathRequestMatcher) || (fc.getRequestMatcher() instanceof AnyRequestMatcher) ) {
				ArrayList<Filter> filters = (ArrayList<Filter>) fc.getFilters();
				
				int index = 0;
				for (Filter filter: filters) {
				    if (filter instanceof TokenAuthenticationFilter) {           
				        break;
				    }
				    index++;
				}				
				
				filters.remove(index);
			}
		}
	}

}
