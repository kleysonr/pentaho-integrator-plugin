package com.pentaho.krios.plugins.osgi.integrator.manager.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentaho.krios.plugins.osgi.integrator.common.services.ITokenService;
import com.pentaho.krios.plugins.osgi.integrator.manager.IntegratorManagerServicesImpl;
import com.pentaho.krios.plugins.osgi.integrator.manager.models.Plugin;

/**
 * REST endpoints' base URI is <webapp>/osgi/cxf/<ADDRESS>/<ENDPOINT_TERMINATION>
 * @see org.pentaho.platform.web.servlet.JAXRSPluginServlet
 */

public class RestAdmin {
	
	@GET
	@Path( "/manager/status" )
	@Produces("application/json")
	public String managerStatus() throws IOException 
	{
		// Get information about the integrator manager
		IntegratorManagerServicesImpl integragorManager = new IntegratorManagerServicesImpl();
		String configPidManager = integragorManager.getConfigPid();
		  
		Plugin pluginManager = new Plugin();
		pluginManager.setConfigFile(IntegratorManagerServicesImpl.getConfigFileLocation(configPidManager));
		pluginManager.setActive(integragorManager.isActive());
		pluginManager.setName(integragorManager.pluginName());
		pluginManager.setVersion(integragorManager.pluginVersion());
		pluginManager.setVendor(integragorManager.pluginVendor());
		pluginManager.setDescription(integragorManager.pluginDescription());
		pluginManager.setUrl(integragorManager.pluginUrl());
		  
		//Output to JSON in String
		ObjectMapper mapper = new ObjectMapper();
		String outputInJson = mapper.writeValueAsString(pluginManager);
		  
		return outputInJson;
	}
	

	@GET
	@Path( "/token/status" )
	@Produces("application/json")
	public String tokenStatus() throws IOException 
	{
		// Output plugin list
		List<Plugin> output = new ArrayList<Plugin>();
	  
		// Get the list of all token providers registered
		List<ITokenService> tokenProviders = IntegratorManagerServicesImpl.getTokenProviders();
		
		// Get information about each token provider
		for (ITokenService provider : tokenProviders)
		{
			String configPid = provider.getConfigPid();
		  
			Plugin plugin = new Plugin();
			plugin.setConfigFile(IntegratorManagerServicesImpl.getConfigFileLocation(configPid));
			plugin.setActive(provider.isActive());
			plugin.setName(provider.pluginName());
			plugin.setVersion(provider.pluginVersion());
			plugin.setVendor(provider.pluginVendor());
			plugin.setDescription(provider.pluginDescription());
			plugin.setUrl(provider.pluginUrl());
			plugin.setProps(IntegratorManagerServicesImpl.getPluginPropsList(provider));
			
			output.add(plugin);
		}

		//Output to JSON in String
		ObjectMapper mapper = new ObjectMapper();
		String outputInJson = mapper.writeValueAsString(output);	  
	  
		return outputInJson;
	}

}
