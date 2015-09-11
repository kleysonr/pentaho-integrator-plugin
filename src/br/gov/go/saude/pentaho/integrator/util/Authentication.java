package br.gov.go.saude.pentaho.integrator.util;

import java.lang.reflect.Method;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang3.StringUtils;

import br.gov.go.saude.pentaho.integrator.security.Auth;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saude do Estado de Goias<br>
 *         www.saude.go.gov.br
 *
 */

public class Authentication {

	public static Map<String, Object> authenticate(HttpServletRequest request, HttpServletResponse response, UriInfo info, String myType, String myToken, String myUrlEncoded) 
	{
		AuthenticationReturn ret = new AuthenticationReturn(true, 200, "", null);
		
		// Checking parameters
		if( "".equalsIgnoreCase(myType) || myType == null || "undefined".equalsIgnoreCase(myType) || "".equalsIgnoreCase(myToken) || myToken == null || "undefined".equalsIgnoreCase(myToken) || "".equalsIgnoreCase(myUrlEncoded) || myUrlEncoded == null || "undefined".equalsIgnoreCase(myUrlEncoded) ) 
		{
			ret.setOk(false);
			ret.setStatus(400);
			ret.setMessage("Integrator Error: Missing type, token and/or urlEncoded.");
		}
		else
		{
		
			try {

				String principalName = null;

				// Reflection for the Auth methods
				String authClasseName = "br.gov.go.saude.pentaho.integrator.security.Auth";
				Class<?> authClasse = Class.forName(authClasseName);
				Auth auth = (Auth) authClasse.newInstance();
				Method authMethod = auth.getClass().getMethod("Auth" + StringUtils.capitalize(myType), String.class, String.class); 
				principalName = (String) authMethod.invoke(authClasse, myToken, myUrlEncoded);
				
				// For simple CORS requests, the server only needs to add these 2 header parameters that allow access to any client.
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				
				if (principalName == null) 
				{
					ret.setOk(false);
					ret.setStatus(400);
					ret.setMessage("Integrator Error: Invalid token.");
				} 
				else 
				{
					if ( AuthenticationHelper.becomeUser( principalName ) != null )
					{
						return ret.getMap();
					} 
					else 
					{
						ret.setOk(false);
						ret.setStatus(400);
						ret.setMessage("Integrator Error: Invalid user.");
					}
				}
				
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();

				ret.setOk(false);
				ret.setStatus(501);
				ret.setMessage("Integrator Error: Type not implemented.");
		
			} catch (Exception e) {
				
				e.printStackTrace();

				ret.setOk(false);
				ret.setStatus(500);
				ret.setMessage("Integrator Error: ERROR.");
				
			} 				

		}
		
		return ret.getMap();
	}

}
