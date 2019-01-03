package br.gov.go.saude.pentaho.integrator.ws;

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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import br.gov.go.saude.pentaho.integrator.util.Authentication;

@Path("/integrator/api")
public class IntegratorREST {

	final static Logger logger = Logger.getLogger(IntegratorREST.class);

	@Context
	private HttpServletRequest request;
	@Context
	private HttpServletResponse response;

	@GET
	@Path("/go")
	public Response redirectLink(@Context UriInfo info) throws URISyntaxException {

		// Get Query Parameters
		String myType = "";
		myType = info.getQueryParameters().getFirst("type");

		String myToken = "";
		myToken = info.getQueryParameters().getFirst("token");

		String myUrlEncoded = "";
		myUrlEncoded = info.getQueryParameters().getFirst("urlEncoded");

		// Call Authenticate Method
		Map<String, Object> ret = Authentication.authenticate(request, response, info, myType, myToken, myUrlEncoded);

		logger.debug("return Call Authentication:" + ret);

		// Authentication Success
		if ((boolean) ret.get("ok")) {

			String myUrl = new String(Base64.decodeBase64(myUrlEncoded.getBytes()));

			URI pentahoBaseUrl = info.getBaseUri().resolve("../../" + myUrl);

			return Response.temporaryRedirect(pentahoBaseUrl).build();
		} else {
			return Response.status((Integer) ret.get("status")).type("text/plain").entity(ret.get("message")).build();
		}

	}

}