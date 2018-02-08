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
import java.util.Collection;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import se.felth.service.starter.central.entity.Library;
import se.felth.service.starter.central.entity.Server;
import se.felth.service.starter.central.entity.Service;

/**
 *
 * @author pa
 */
@Path("libraries")
@Stateless
public class LibraryResource {
	@Inject
	Boundary b;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Library> get() {
		return b.getLibraries();
	}
	
	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Library getOne(@PathParam("id") String id) {
		return b.getLibrary(id);
	}
	
	@GET
	@Path("{id}/artifact")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public InputStream getArtifact(@PathParam("id") String id) throws FileNotFoundException {
		Library d = b.getLibrary(id);
		return b.getArtifactByLibraryId(id);
	}
	
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Library post(Library s) {
		
		return b.addLibrary(s);
	}
	
        @PUT
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Library put(@PathParam("id") String id, Library s) {
		if (s == null || !Objects.equals(id, s.getId())) {
			throw new WebApplicationException(400);
		}
		
		return b.updateLibrary(s);
	}
}
