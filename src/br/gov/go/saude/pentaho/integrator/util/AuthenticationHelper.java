package br.gov.go.saude.pentaho.integrator.util;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saúde do Estado de Goiás<br>
 *         www.saude.go.gov.br
 *         
 * @contribution Marcello Pontes<br>
 * 				 www.oncase.com.br
 *
 */

import java.util.ArrayList;
import java.util.List;

import org.pentaho.platform.api.engine.IPentahoSession;
import org.pentaho.platform.api.engine.IUserRoleListService;
import org.pentaho.platform.api.mt.ITenant;
import org.pentaho.platform.api.mt.ITenantedPrincipleNameResolver;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;
import org.pentaho.platform.engine.core.system.UserSession;
import org.springframework.security.Authentication;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.User;

public class AuthenticationHelper {

	public static IPentahoSession becomeUser( final String principalName ) 
	{
		IPentahoSession session = null;
		ITenantedPrincipleNameResolver tenantedUserNameUtils = getTenantedUserNameUtils();
		
		if ( tenantedUserNameUtils != null ) {

			session = PentahoSessionHolder.getSession();
			ITenant tenant = tenantedUserNameUtils.getTenant( principalName );
			session.setAuthenticated( principalName );
			session.setAttribute( IPentahoSession.TENANT_ID_KEY, tenant.getId() );

		} else {
			
			session = new UserSession( principalName, null, false, null );
			session.setAuthenticated( principalName );
			PentahoSessionHolder.getSession().setAuthenticated(principalName);
			
		}
		
		PentahoSessionHolder.setSession( session );
		Authentication auth = createAuthentication( principalName );
		
		if ( auth == null ) return null;

		SecurityContextHolder.getContext().setAuthentication( auth );
		PentahoSystem.sessionStartup( PentahoSessionHolder.getSession(), null );

		return session;
	}

	private static ITenantedPrincipleNameResolver getTenantedUserNameUtils() 
	{
		return PentahoSystem.get( ITenantedPrincipleNameResolver.class, "tenantedUserNameUtils", null );
	}

	private static Authentication createAuthentication( String principalName ) 
	{
		GrantedAuthority[] grantedAuthorities = null;
		
		String anonymousUser = PentahoSystem.getSystemSetting( "anonymous-authentication/anonymous-user", "anonymousUser" ); //$NON-NLS-1$//$NON-NLS-2$
		IUserRoleListService userRoleListService = getUserRoleListService();
		
		List<String> roles = new ArrayList<String>();
		
		
		if ( anonymousUser.equals( principalName ) ) {

			String anonymousRole = PentahoSystem.getSystemSetting( "anonymous-authentication/anonymous-role", "Anonymous" ); //$NON-NLS-1$//$NON-NLS-2$
			roles.add( anonymousRole );
			
		} else {
			
			roles = userRoleListService.getRolesForUser( null, principalName );
			
		}
		
		grantedAuthorities = new GrantedAuthority[ roles.size() ];
		
		for ( int i = 0; i < roles.size(); i++ ) {
			grantedAuthorities[ i ] = new GrantedAuthorityImpl( roles.get( i ) );
		}
		
		if ( grantedAuthorities == null ) return null;
		
		User user = new User( principalName, "", true, true, true, true, grantedAuthorities );
		Authentication auth = new UsernamePasswordAuthenticationToken( user, null, grantedAuthorities );
		
		return auth;
	}
	
	private static IUserRoleListService getUserRoleListService() 
	{
		
		 return PentahoSystem.get( IUserRoleListService.class );
		 
	}	
}
