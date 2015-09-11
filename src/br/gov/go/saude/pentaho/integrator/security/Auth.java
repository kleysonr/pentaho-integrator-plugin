package br.gov.go.saude.pentaho.integrator.security;

/**
 * 
 * @author Kleyson Rios<br>
 *         Secretaria de Saude do Estado de Goias<br>
 *         www.saude.go.gov.br
 *
 */

import java.sql.SQLException;

public class Auth {

	// Authentication based on a token
	public static String AuthToken(String myToken, String myUrlEncoded) throws SQLException, Exception
	{
		return Database.getUsernameFromToken( myToken, myUrlEncoded );
	}
	
	// TODO
	// Authentication based on the Google Authenticator TOTP (Time-based One-time Password Algorithm)
	/*
	private String AuthTotp(String myToken, String myUrlEncoded) throws SQLException, Exception
	{
		return null;
	}
	*/

}
