/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.jackson.JacksonFeature;

/**
 *
 * @author pa
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> resources = new java.util.HashSet<>();
		resources.add(JacksonFeature.class);
		addRestResourceClasses(resources);
		return resources;
	}

	/**
	 * Do not modify addRestResourceClasses() method.
	 * It is automatically populated with
	 * all resources defined in the project.
	 * If required, comment out calling this method in getClasses().
	 */
	private void addRestResourceClasses(Set<Class<?>> resources) {
		resources.add(se.felth.service.starter.central.boundary.LibraryResource.class);
		resources.add(se.felth.service.starter.central.boundary.MavenRepositoryResource.class);
		resources.add(se.felth.service.starter.central.boundary.ServerDeploymentResource.class);
		resources.add(se.felth.service.starter.central.boundary.ServerResource.class);
        resources.add(se.felth.service.starter.central.boundary.ServiceResource.class);
	}
	
}
