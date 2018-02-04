/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.swing.event.ListSelectionEvent;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import se.felth.service.starter.central.entity.Deployment;
import se.felth.service.starter.central.entity.Environment;
import se.felth.service.starter.central.entity.Library;
import se.felth.service.starter.central.entity.Server;
import se.felth.service.starter.central.entity.ServerDeployment;
import se.felth.service.starter.central.entity.Service;
import se.felth.service.starter.central.entity.ServiceVersion;

/**
 *
 * @author pa
 */
@Singleton
public class Boundary {

	@Inject
	MongoDatabase db;
	@Inject
	Datastore ds;
	private static final Logger LOG = Logger.getLogger(Boundary.class.getName());

	@PostConstruct
	public void init() {
		/*
		try {
			
			servers = new HashMap<>();
			environments = new HashMap<>();
			deployments = new HashMap<>();
			services = new HashMap<>();
			libraries = new HashMap<>();
			
			Server server = new Server("s1");
			servers.put(server.getName(), server);
			
			Environment e = new Environment("test1");
			environments.put(e.getName(), e);
			
			Library l = new Library(UUID.randomUUID().toString(), "MySQL JDBC driver", new URI("file:///Users/pa/mysql-connector-java-5.1.23-bin.jar"));
			libraries.put(l.getId(), l);
			
			ServiceVersion v = new ServiceVersion();
			v.setName("1.0");
			v.setArtifactLocation(new URI("file:///Users/pa/NetBeansProjects/demo/target/demo-1.0-SNAPSHOT.war"));
			v.setLibraries(Arrays.asList(l));
			
			Service s = new Service();
			s.setName("Demo");
			s.getVersions().add(v);
			Deployment d = new Deployment();
			
			d.setId(UUID.randomUUID().toString());
			d.setServerIds(Arrays.asList(server.));
			d.setServiceVersion(v);
			deployments.put(d.getId(), d);
		} catch (URISyntaxException ex) {
			Logger.getLogger(Boundary.class.getName()).log(Level.SEVERE, null, ex);
		}
		 */
	}

	public List<Service> getServices() {
		return ds.find(Service.class).asList();
	}
	
	public Service getService(String id) {
		return getService(new ObjectId(id));
	}
	
	public Service getService(ObjectId id) {
		return ds.find(Service.class).filter("id ==", id).get();
	}
	
	public Service addService(Service s) {
		Key<Service> k = ds.save(s);
		LOG.info(k.getId().getClass().getName());
		return getService((ObjectId)k.getId());
	}
	
	public Library addLibrary(Library s) {
		Key<Library> k = ds.save(s);
		
		return getLibrary((ObjectId)k.getId());
	}
	
	public Service updateService(Service s) {
		ds.save(s);
		
		return getService(s.getId());
	}
	
	public List<Server> getServers() {
		return ds.find(Server.class).asList();
	}
	
	public Server getServer(String id) {
		return ds.find(Server.class).filter("id ==", new ObjectId(id)).get();
	}
	
	public Server addServer(Server s) {
		Key<Server> k = ds.save(s);
		LOG.info(k.getId().getClass().getName());
		return getServer((String)k.getId());
	}

	public FileInputStream getArtifactByDeploymentName(String deploymentName) throws FileNotFoundException {
		URI artifactLocation;

		for (Service s : getServices()) {
			for (ServiceVersion sv : s.getVersions()) {
				for (Deployment d : sv.getDeployments()) {
					if (d.getName().equals(deploymentName)) {
						artifactLocation = sv.getArtifactLocation();

						return new FileInputStream(new File(artifactLocation));

					}
				}
			}
		}

		return null;
	}
	
	public FileInputStream getArtifactByLibraryId(String libraryId) throws FileNotFoundException {
		

		return new FileInputStream(new File(getLibrary(libraryId).getArtifactLocation()));
		
		
	}

	public List<ServerDeployment> getDeploymentsForServer(String server) {
		List<ServerDeployment> list = new ArrayList<>();

		for (Service s : getServices()) {
			for (ServiceVersion sv : s.getVersions()) {
				for (Deployment d : sv.getDeployments()) {
					if (d.getServerIds().contains(server)) {
						ServerDeployment sd = new ServerDeployment();
						sd.setArtifactLocation(sv.getArtifactLocation());
						sd.setEnableHz(d.getEnableHz());
						sd.setHttpPort(d.getHttpPort());
						sd.setHttpsPort(d.getHttpsPort());
						sd.setHzPort(d.getHzPort());
						sd.setLibraryIds(sv.getLibraryIds());
						sd.setName(d.getName());
						sd.setPingPath(d.getPingPath());
						sd.setServerIds(d.getServerIds());
						sd.setServiceName(s.getName());
						sd.setServiceVersionName(s.getName());
						sd.setServiceProperties(d.getServiceProperties());
						sd.setJdbcConnections(d.getJdbcConnections());
						
						list.add(sd);
					}
				}
			}

		}

		return list;
		//Server s = servers.get(server);
		//return deployments.values().stream().filter(d -> d.getServers().contains(s)).collect(Collectors.toList());
	}

	public List<Library> getLibraries() {
		return ds.find(Library.class).asList();
	}

	public Library getLibrary(String id) {
		return getLibrary(new ObjectId(id));
	}
	
	public Library getLibrary(ObjectId id) {
		return ds.find(Library.class).filter("id == ", id).get();
	}
	
	
	
}
