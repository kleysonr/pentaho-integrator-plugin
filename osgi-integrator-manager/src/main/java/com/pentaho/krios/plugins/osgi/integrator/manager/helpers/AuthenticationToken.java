package com.pentaho.krios.plugins.osgi.integrator.manager.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

public class AuthenticationToken extends AbstractAuthenticationToken implements Serializable{
	
	private final String secret;
	private Object principal;
	private ArrayList<GrantedAuthority> authorities;

	/**
	 * Constructs an instance.
	 * 
	 * @param principalName
	 *          the value of the username to be authenticated. Value
	 *          cannot be <code>null</code>.
	 * @throws IllegalArgumentException
	 *           if <code>principalName</code> is <code>null</code>.
	 */
	public AuthenticationToken(String secret) {
		super(null);

	    Assert.notNull(secret);

	    this.secret = secret;
	    this.authorities = new ArrayList<GrantedAuthority>();
	}

	/**
	 * Reports the secret stored within this instance.
	 * 
	 * @return the secret.
	 */
	public String getSecret() {
		return secret;
	}	

	public Object getCredentials() {
		return getSecret();
	}

	public Object getPrincipal() {
	    Object result = principal;

	    if (result == null) {
	      result = getDetails();
	    }

	    if (result == null) {
	      result = secret;
	    }

	    return result;
	}
	
	  /**
	   * Sets the roles or {@link GrantedAuthority granted authorities} associated
	   * with this instance.
	   * 
	   * @param collection
	   *          the list of roles. The instance and any of its indexes cannot be
	   *          <code>null</code>.
	   * @throws IllegalArgumentException
	   *           if <code>auth</code> or any of its member values is
	   *           <code>null</code>.
	   */
		public void setAuthorities(Collection<? extends GrantedAuthority> collection) {
			Assert.notNull(collection);
			this.authorities = new ArrayList<GrantedAuthority>(collection);
		}	


}
