package com.pentaho.krios.plugins.osgi.integrator.manager.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;

import com.pentaho.krios.plugins.osgi.integrator.common.services.ITokenService;
import com.pentaho.krios.plugins.osgi.integrator.manager.IntegratorManagerServicesImpl;
import com.pentaho.krios.plugins.osgi.integrator.manager.helpers.AuthenticationHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;

public class TokenAuthenticationFilter extends GenericFilterBean {
	
    @SuppressWarnings("unused")
	private static final long serialVersionUID = 1280540339612668867L;

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		// Get all security headers from the request
		HashMap<String, String[]> secParams = new HashMap<String, String[]>();
		Map<String, String[]> queryParams = request.getParameterMap();
		
		for (Entry<String, String[]> entry : queryParams.entrySet()) 
		{
			if (entry.getKey().matches("^_[a-zA-Z0-9]+")) 
			{
				secParams.put(entry.getKey(), entry.getValue());
				
				System.out.println("Security Param: " + entry.getKey() + " / " + entry.getValue()[0]);
			}
		}
		
		// Call the authenticate method from registered token providers
		if (!secParams.isEmpty())
		{
			List<ITokenService> tokenProviders = IntegratorManagerServicesImpl.getTokenProviders();
			
			for (ITokenService provider : tokenProviders)
			{
				if ( provider.isActive() )
				{
					
					System.out.println( "Calling: " + provider.pluginName() );
					
					String principal = provider.authenticate(secParams);
	
					if (principal != null) 
					{
						AuthenticationHelper.becomeUser(principal);
						
						HttpSession httpSession = ((HttpServletRequest) request).getSession();
			            httpSession.setAttribute( PentahoSystem.PENTAHO_SESSION_KEY, PentahoSessionHolder.getSession() );
	
			            // definition of anonymous inner class
			            SecurityContext authWrapper = new SecurityContext() 
			            {
			            	private static final long serialVersionUID = 1L;
			            	private Authentication authentication;
	
			            	public Authentication getAuthentication() {
			            		return authentication;
			            	};
	
			            	public void setAuthentication( Authentication authentication ) {
			            		this.authentication = authentication;
			            	};
			            }; // end anonymous inner class
	
			            authWrapper.setAuthentication( SecurityContextHolder.getContext().getAuthentication() );
			            httpSession.setAttribute( HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, authWrapper );
						
						System.out.println("User authenticated");
					}
				}
				else
				{
					System.out.println( provider.pluginName() + " is inactive." );
				}
			}
		}
		
		System.out.println("********** CustomHttpFirewall" + ((HttpServletRequest) request).getRequestURL().toString());
		 
		chain.doFilter(request, response);

	}
	
}