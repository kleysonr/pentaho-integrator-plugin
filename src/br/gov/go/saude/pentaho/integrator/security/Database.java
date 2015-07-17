package br.gov.go.saude.pentaho.integrator.security;

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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.pentaho.platform.api.engine.IPluginManager;
import org.pentaho.platform.engine.core.system.PentahoSessionHolder;
import org.pentaho.platform.engine.core.system.PentahoSystem;

import br.gov.go.saude.pentaho.integrator.engine.PluginConfig;

public class Database {

	public static String getUsernameFromToken (String token, String url) throws SQLException, Exception {

		String username = null;
		Connection conn = getDatabaseConnection();
		
		String tokenQuery = PluginConfig.props.getProperty("TokenQuery");
		String tokenQueryCount = PluginConfig.props.getProperty("TokenQueryCount");
		String tokenQueryDelete = PluginConfig.props.getProperty("TokenQueryDelete");

		try {
		
			PreparedStatement stmtCount = conn.prepareStatement(tokenQueryCount);
			PreparedStatement stmt = conn.prepareStatement(tokenQuery);
			PreparedStatement stmtDel = conn.prepareStatement(tokenQueryDelete);
			
			try {
			
				stmtCount.setString(1, token);
				stmtCount.setString(2, url);
				
				// Get count...
				int rowCount = 0;

				ResultSet rs = stmtCount.executeQuery();
				
				try {
					rs.next();
					rowCount = rs.getInt(1);
				} finally {
					rs.close();
				}
				
				if (rowCount == 0) {
					return username;
				}
				
				// Get username
				stmt.setString(1, token);
				stmt.setString(2, url);
				
				rs = stmt.executeQuery();

				try {
					rs.next();
					username = rs.getString(1);
				} finally {
					rs.close();
				}
				
				// Delete Token
				stmtDel.setString(1, token);
				stmtDel.setString(2, url);
				
				stmtDel.execute();

			} finally {
				stmt.close();
				stmtCount.close();
				stmtDel.close();
			}
			
		} finally {
			conn.close();
		}
		
		return username;
	}

	private static Connection getDatabaseConnection() 
	{
		IPluginManager plugMan = PentahoSystem.get(IPluginManager.class, PentahoSessionHolder.getSession());
		DataSource datasourceDS = null;
		
		try
		{
		    Object integratorFactory = plugMan.getBean("integratorTemplateFactory");
		    Method jndiGetter = integratorFactory.getClass().getMethod("getJndiDatabaseConn");
		    datasourceDS = (DataSource) jndiGetter.invoke(integratorFactory);
		    
		    return datasourceDS.getConnection();
		}
		catch(Exception e)
		{
		    System.out.println("Integrator Plugin: Hey, sorry! There was a problem generating your template");
		    e.printStackTrace();
		    return null;
		}
	}	

}
