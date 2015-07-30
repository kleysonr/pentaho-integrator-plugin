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

import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import br.gov.go.saude.pentaho.integrator.util.Authentication;
import br.gov.go.saude.pentaho.integrator.util.AuthenticationReturn;


@Path("/integrator/api")
public class IntegratorREST {

	@Context private HttpServletRequest request;
	@Context private HttpServletResponse response;

	@GET
	@Path("/go")
	public Response redirectLink(@Context UriInfo info) throws URISyntaxException 
	{
		
		String myType = "";
		myType = info.getQueryParameters().getFirst("type");

		String myToken = "";
		myToken = info.getQueryParameters().getFirst("token");
		
		String myUrlEncoded = "";
		myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");

		AuthenticationReturn ret =  Authentication.authenticate(request, response, info, myType, myToken, myUrlEncoded);
		
		if (ret.isOk()) return Response.temporaryRedirect(ret.getUrl()).build();
		else return Response.status(ret.getStatus()).type("text/plain").entity(ret.getMessage()).build();
		
	}

	@GET
	public Response tst(@Context UriInfo info) throws URISyntaxException 
	{
		
		return Response.status(200).type("text/plain").entity("ok").build();
		
	}
	
}