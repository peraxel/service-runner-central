/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import java.util.List;
import java.util.Objects;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import se.felth.service.starter.central.entity.Service;

/**
 *
 * @author pa
 */
@Stateless
@Path("services")
public class ServiceResource {
	@Inject
	Boundary b;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Service> get() {
		return b.getServices();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Service getOne(@PathParam("id") String id) {
		return b.getService(id);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Service post(Service s) {
		
		return b.addService(s);
	}
	
	@PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Service put(@PathParam("id") String id, Service s) {
		if (s == null || !Objects.equals(id, s.getId())) {
			throw new WebApplicationException(400);
		}
		
		return b.updateService(s);
	}
}
