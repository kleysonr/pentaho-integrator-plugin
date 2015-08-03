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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;

import br.gov.go.saude.pentaho.integrator.util.Authentication;


@Path("/integrator/api")
public class IntegratorREST {

	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;

	@GET
	@Path("/go")
	public Response redirectLink(@Context UriInfo info) throws URISyntaxException 
	{

		// Get Query Parameters
		String myType = "";
		myType = info.getQueryParameters().getFirst("type");

		String myToken = "";
		myToken = info.getQueryParameters().getFirst("token");
		
		String myUrlEncoded = "";
		myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");

		// Call Authenticate Method
		Map<String, Object> ret =  Authentication.authenticate(request, response, info, myType, myToken, myUrlEncoded);
		
		// Authentication Success
		if ((boolean) ret.get("ok")) {

			String myUrl = new String( Base64.decodeBase64(myUrlEncoded.getBytes()) );

			URI pentahoBaseUrl = null;
			
			try 
			{
				// Workaround for java.net.URISyntaxException: Illegal character
				pentahoBaseUrl = info.getBaseUri().resolve("../../" + URLEncoder.encode(URLDecoder.decode(myUrl, "UTF-8"), "UTF-8").replaceAll("\\%2[fF]", "/").replaceAll("\\+", "%20") );
			} 
			catch (UnsupportedEncodingException e) 
			{
				e.printStackTrace();
				return Response.status(500).type("text/plain").entity("Integrator Error: ERROR.").build();
							
			}
			
			return Response.temporaryRedirect(pentahoBaseUrl).build();
		}
		else 
		{
			return Response.status((Integer) ret.get("status")).type("text/plain").entity(ret.get("message")).build();
		}
		
		
	}

}