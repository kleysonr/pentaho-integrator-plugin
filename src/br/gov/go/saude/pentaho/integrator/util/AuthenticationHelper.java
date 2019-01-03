package br.gov.go.saude.pentaho.integrator.util;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saude do Estado de Goias<br>
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
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

public class AuthenticationHelper {

	public static IPentahoSession becomeUser(final String principalName) {
		IPentahoSession session = null;
		ITenantedPrincipleNameResolver tenantedUserNameUtils = getTenantedUserNameUtils();

		if (tenantedUserNameUtils != null) {

			session = PentahoSessionHolder.getSession();
			ITenant tenant = tenantedUserNameUtils.getTenant(principalName);
			session.setAuthenticated(principalName);
			session.setAttribute(IPentahoSession.TENANT_ID_KEY, tenant.getId());

		} else {
			session = new UserSession(principalName, null, false, null);
			session.setAuthenticated(principalName);
			PentahoSessionHolder.getSession().setAuthenticated(principalName);
		}

		PentahoSessionHolder.setSession(session);
		AbstractAuthenticationToken auth = createAuthentication(principalName);

		if (auth == null)
			return null;

		SecurityContextHolder.getContext().setAuthentication(auth);
		PentahoSystem.sessionStartup(PentahoSessionHolder.getSession(), null);

		return session;
	}

	private static ITenantedPrincipleNameResolver getTenantedUserNameUtils() {
		return PentahoSystem.get(ITenantedPrincipleNameResolver.class, "tenantedUserNameUtils", null);
	}

	private static UsernamePasswordAuthenticationToken createAuthentication(String principalName) {
		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();
	
		String anonymousUser = PentahoSystem.getSystemSetting("anonymous-authentication/anonymous-user", //$NON-NLS-1$
				"anonymousUser"); //$NON-NLS-1$
		IUserRoleListService userRoleListService = getUserRoleListService();

		List<String> roles = new ArrayList<String>();

		if (anonymousUser.equals(principalName)) {
			String anonymousRole = PentahoSystem.getSystemSetting("anonymous-authentication/anonymous-role", //$NON-NLS-1$
					"Anonymous"); //$NON-NLS-1$
			roles.add(anonymousRole);

		} else 
			roles = userRoleListService.getRolesForUser(null, principalName);

		for (int i = 0; i < roles.size(); i++) 
			grantedAuthorities.add(new SimpleGrantedAuthority(roles.get(i)));

		User user = new User(principalName, "", grantedAuthorities);
		
		return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
	}

	private static IUserRoleListService getUserRoleListService() {
		return PentahoSystem.get(IUserRoleListService.class);
	}
}
