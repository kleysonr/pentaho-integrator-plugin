package com.pentaho.krios.plugins.osgi.integrator.manager.helpers;

/**
 * 
 * @author Kleyson Rios (krios@pentaho.com)<br>
 *
 */
import java.util.Locale;

import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.mt.ITenant;
import org.pentaho.platform.api.mt.ITenantedPrincipleNameResolver;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.core.system.UserSession;
import org.pentaho.platform.engine.security.SecurityHelper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class AuthenticationHelper {
	
	private static ApplicationEventPublisher eventPublisher;

	// Cloned from SecurityHelper and adapted to add a default location to the session
	public static UserSession becomeUser( final String principalName ) 
	{
		UserSession session = null;
		
		try 
		{
			// Check if user exists before try to authenticate it
			UserDetailsService userDetailsService = PentahoSystem.get( UserDetailsService.class );
			//UserDetails user = userDetailsService.loadUserByUsername( principalName ); 
			
			Locale locale = Locale.getDefault();
			ITenantedPrincipleNameResolver tenantedUserNameUtils = getTenantedUserNameUtils();
			
			if ( tenantedUserNameUtils != null ) 
			{
				session = new UserSession( principalName, locale, false, null );
				ITenant tenant = tenantedUserNameUtils.getTenant( principalName );
				session.setAttribute( IPentahoSession.TENANT_ID_KEY, tenant.getId() );
				session.setAuthenticated( tenant.getId(), principalName );
			}
			else
			{
				session = new UserSession( principalName, locale, false, null );
				session.setAuthenticated( principalName );
			}
			
			PentahoSessionHolder.setSession( session );
			
			Authentication auth = SecurityHelper.getInstance().createAuthentication( principalName );
		
			if ( auth == null ) return null;
			
			// Clearing the SecurityContext to force the subsequent call to getContext() to generate a new SecurityContext.
		    // This prevents us from modifying the Authentication on a SecurityContext isntance which may be shared between
		    // threads.
		    //PentahoSessionHolder.getSession().setAttribute( IPentahoSession.SESSION_ROLES, auth.getAuthorities() );
		    //SecurityContextHolder.clearContext();
	
			SecurityContextHolder.getContext().setAuthentication( auth );
			PentahoSystem.sessionStartup( PentahoSessionHolder.getSession(), null );
			
			// New for 5.0 -- this ensures the startup action gets called....
			//eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(auth, AuthenticationHelper.class.getClass()));
		}
		catch (UsernameNotFoundException e)
		{
			System.out.println(e.getMessage() + ": " + principalName);
			e.printStackTrace();
			
			return null;
		}
		
		return session;
	}

	private static ITenantedPrincipleNameResolver getTenantedUserNameUtils() 
	{
		return PentahoSystem.get( ITenantedPrincipleNameResolver.class, "tenantedUserNameUtils", null );
	}

	
}