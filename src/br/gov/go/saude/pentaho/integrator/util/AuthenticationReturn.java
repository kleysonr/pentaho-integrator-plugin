package br.gov.go.saude.pentaho.integrator.util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saude do Estado de Goias<br>
 *         www.saude.go.gov.br
 *
 */

public class AuthenticationReturn {

	Boolean ok;
	Integer status;
	String message;
	URI url;
	
	public AuthenticationReturn(Boolean ok, Integer status, String message, URI url) 
	{
		this.status = status;
		this.message = message;
		this.ok = ok;
		this.url = url;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean isOk() {
		return ok;
	}

	public void setOk(Boolean ok) {
		this.ok = ok;
	}

	public URI getUrl() {
		return url;
	}

	public void setUrl(URI url) {
		this.url = url;
	}

	
	public Map<String, Object> getMap() {
		HashMap<String, Object> map = new HashMap<>();
		
		map.put("status", status);
		map.put("message", message);
		map.put("ok", ok);
		
		return map;
	}
}
