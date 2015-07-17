package br.gov.go.saude.pentaho.integrator.ws;

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

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import com.sun.jersey.api.view.Viewable;

import br.gov.go.saude.pentaho.integrator.security.Database;
import br.gov.go.saude.pentaho.integrator.util.AuthenticationHelper;


@Path("/integrator/api")
public class IntegratorREST {

	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;
	
	@Context private ServletContext _context;

	@GET
	@Path("/go")
	public Response redirectLink(@Context UriInfo info) throws URISyntaxException {
		
		String myType = "";
		myType = info.getQueryParameters().getFirst("type");

		String myToken = "";
		myToken = info.getQueryParameters().getFirst("token");
		
		String myUrlEncoded = "";
		myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");

		// Checking parameters
		if( "".equalsIgnoreCase(myType) || myType == null || "".equalsIgnoreCase(myToken) || myToken == null || "".equalsIgnoreCase(myUrlEncoded) || myUrlEncoded == null ) 
		{
			return Response.status(400).type("text/plain").entity("Integrator Error: Missing type, token and/or urlEncoded.").build();
		}
		else
		{
		
			try {
			
				String principalName = null;

				// Reflection for the auth method
				Class<? extends IntegratorREST> classe = this.getClass();
				Method authMethod = classe.getDeclaredMethod("Auth" + StringUtils.capitalize(myType), String.class, String.class);
				principalName = (String) authMethod.invoke(this, myToken, myUrlEncoded);
				
				// For simple CORS requests, the server only needs to add these 2 header parameters that allow access to any client.
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				
				if (principalName == null) 
				{
					return Response.status(400).type("text/plain").entity("Integrator Error: Invalid token.").build();
				} 
				else 
				{
					if ( AuthenticationHelper.becomeUser( principalName ) != null )
					{
						String myUrl = new String( Base64.decodeBase64(myUrlEncoded.getBytes()) );

						// Workaround for java.net.URISyntaxException: Illegal character
						URI pentahoBaseUrl = info.getBaseUri().resolve("../../" + URLEncoder.encode(URLDecoder.decode(myUrl, "UTF-8"), "UTF-8").replaceAll("\\%2[fF]", "/").replaceAll("\\+", "%20") );
						
		 				return Response.temporaryRedirect(pentahoBaseUrl).build(); 
					} 
					else 
					{
						return Response.status(400).type("text/plain").entity("Integrator Error: Invalid user.").build();
					}

				}
				
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
				return Response.status(501).type("text/plain").entity("Integrator Error: Type not implemented.").build();
		
			} catch (Exception e) {
				
				e.printStackTrace();
				return Response.status(500).type("text/plain").entity("Integrator Error: ERROR.").build();
				
			}
			
		}
		
	}

	@POST
	@Path("/go")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Object forwardLink(@Context UriInfo info) throws URISyntaxException {

		String myType = "";
		myType = info.getQueryParameters().getFirst("type");

		String myToken = "";
		myToken = info.getQueryParameters().getFirst("token");
		
		String myUrlEncoded = "";
		myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");

		// Checking parameters
		if( "".equalsIgnoreCase(myType) || "".equalsIgnoreCase(myToken) || "".equalsIgnoreCase(myUrlEncoded) ) 
		{
			//return Response.ok("Integrator Error: Missing token, type and/or urlEncoded parameter(s).").type("text/plain").build();
			return null;
		}
		else
		{
		
			try {
			
				String principalName = null;

				// Reflection for the auth method
				Class<? extends IntegratorREST> classe = this.getClass();
				Method authMethod = classe.getDeclaredMethod("Auth" + StringUtils.capitalize(myType), String.class, String.class);
				principalName = (String) authMethod.invoke(this, myToken, myUrlEncoded);
				
				// For simple CORS requests, the server only needs to add these 2 header parameters that allow access to any client.
				response.setHeader("Access-Control-Allow-Origin", "*");
				response.setHeader("Access-Control-Allow-Credentials", "true");
				
				if (principalName == null) 
				{
					//return Response.ok("Integrator Error: Invalid token.").type("text/plain").build();
					return null;
				} 
				else 
				{
					if ( AuthenticationHelper.becomeUser( principalName ) != null )
					{
						String myUrl = new String( Base64.decodeBase64(myUrlEncoded.getBytes()) );

						// Workaround for java.net.URISyntaxException: Illegal character
						String pentahoBaseUrl = URLEncoder.encode(URLDecoder.decode(myUrl, "UTF-8"), "UTF-8").replaceAll("\\%2[fF]", "/").replaceAll("\\+", "%20"); 
		 				//return Response.temporaryRedirect(pentahoBaseUrl).build();
						
						RequestDispatcher rd = _context.getRequestDispatcher(pentahoBaseUrl);
						rd.forward(request, response);
						
						//return Response.ok().build();
						return null;
						
						//return new Viewable(pentahoBaseUrl, null);

					} 
					else 
					{
						//return Response.ok("Integrator Error: Invalid user.").type("text/plain").build();
						return null;

					}

				}
				
			} catch (NoSuchMethodException e) {
				
				e.printStackTrace();
				//return Response.ok("Integrator Error: Type not implemented.").type("text/plain").build();
				return null;
		
			} catch (Exception e) {
				
				e.printStackTrace();
				//return Response.ok("Integrator Error: ERROR.").type("text/plain").build();
				return null;
				
			}
			
		}
		
	}
	
	// Authentication based on a token
	private String AuthToken(String myToken, String myUrlEncoded) throws SQLException, Exception
	{
		return Database.getUsernameFromToken( myToken, myUrlEncoded );
	}
	
	// TODO
	// Authentication based on the Google Authenticator TOTP (Time-based One-time Password Algorithm)
	private String AuthTotp(String myToken, String myUrlEncoded) throws SQLException, Exception
	{
		return null;
	}

}