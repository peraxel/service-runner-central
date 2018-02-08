/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import se.felth.service.starter.central.entity.MavenRepository;
import se.felth.service.starter.central.entity.Server;
import se.felth.service.starter.central.entity.Service;

/**
 *
 * @author pa
 */
@Stateless
@Path("maven_repositories")
public class MavenRepositoryResource {
	@Inject
	Boundary b;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<MavenRepository> get() {
		return b.getMavenRepositories();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public MavenRepository getOne(@PathParam("id") String id) {
		return b.getMavenRepository(id);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public MavenRepository post(MavenRepository s) {
		
		return b.addMavenRepository(s);
	}
}
