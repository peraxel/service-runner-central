/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import se.felth.service.starter.central.entity.ServerDeployment;

/**
 *
 * @author pa
 */
@Path("server-deployments")
@Stateless
public class ServerDeploymentResource {
	@Inject
	Boundary b;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ServerDeployment> get(@QueryParam("server") String server) {
		return b.getDeploymentsForServer(server);
	}
	
	@GET
	@Path("{name}/artifact")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public InputStream getArtifact(@PathParam("name") String name) throws FileNotFoundException {
		return b.getArtifactByDeploymentName(name);
		
	}
}
