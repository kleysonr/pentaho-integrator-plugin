package com.pentaho.krios.plugins.osgi.integrator.manager.filterchain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.firewall.RequestRejectedException;

import com.pentaho.krios.plugins.osgi.integrator.manager.IntegratorManagerServicesImpl;
import com.pentaho.krios.plugins.osgi.integrator.manager.helpers.AuthenticationHelper;
import com.pentaho.krios.plugins.osgi.integrator.manager.services.ITokenService;

public class CustomHttpFirewall extends DefaultHttpFirewall {
	
	//static Logger logger = Logger.getLogger (Activator.class.getName());
	
	@Override
	public FirewalledRequest getFirewalledRequest(HttpServletRequest request) throws RequestRejectedException 
	{
		// Perform default URL validation
		FirewalledRequest fwr = super.getFirewalledRequest(request);

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
						
						HttpSession httpSession = request.getSession();
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
		
		System.out.println("********** CustomHttpFirewall" + request.getRequestURL().toString());
		 
		return fwr;
	}	
	
	@Override
	public HttpServletResponse getFirewalledResponse(HttpServletResponse response) {
		return super.getFirewalledResponse(response);
	}	

}
