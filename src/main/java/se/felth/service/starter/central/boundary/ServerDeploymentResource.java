/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import com.auth0.jwt.JWTVerifier;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import se.felth.service.starter.central.PkiSecured;
import se.felth.service.starter.central.entity.DeploymentInstance;

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
    @PkiSecured
    public List<DeploymentInstance> get(@HeaderParam("Authorization") String authHeader, @Context HttpServletRequest request) {
        String serverId = b.serverAuth(authHeader);
        if (serverId == null) {
            throw new WebApplicationException(401);
        }
        
        return b.getDeploymentsForServer(serverId);
    }

    @GET
    @Path("{name}/artifact")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public InputStream getArtifact(@PathParam("name") String name) throws FileNotFoundException {
        return b.getArtifactByDeploymentName(name);

    }
    
}
