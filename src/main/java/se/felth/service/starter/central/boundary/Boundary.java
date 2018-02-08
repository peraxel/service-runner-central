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
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.swing.event.ListSelectionEvent;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import se.felth.service.starter.central.entity.Deployment;
import se.felth.service.starter.central.entity.Environment;
import se.felth.service.starter.central.entity.Gav;
import se.felth.service.starter.central.entity.Library;
import se.felth.service.starter.central.entity.MavenRepository;
import se.felth.service.starter.central.entity.Server;
import se.felth.service.starter.central.entity.DeploymentInstance;
import se.felth.service.starter.central.entity.Service;
import se.felth.service.starter.central.entity.ServiceVersion;

/**
 *
 * @author pa
 */
@Singleton
public class Boundary {

    static final String ARTIFACT_LOCATION_TYPE_DISK = "disk";
    static final String ARTIFACT_LOCATION_TYPE_MAVEN_REPOSITORY = "mvn_repo";

    @Inject
    MongoDatabase db;
    @Inject
    Datastore ds;
    private static final Logger LOG = Logger.getLogger(Boundary.class.getName());

    @PostConstruct
    public void init() {

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
        return getService((ObjectId) k.getId());
    }

    public Library addLibrary(Library s) {
        Key<Library> k = ds.save(s);

        return getLibrary((ObjectId) k.getId());
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
        return getServer((String) k.getId());
    }

    public InputStream readArtifactFromDisk(URI artifactLocation) throws FileNotFoundException {
        return new FileInputStream(new File(artifactLocation));
    }

    public InputStream readArtifactFromMvnRepo(String repositoryId, Gav gav) throws FileNotFoundException {
        MavenRepository repo = getMavenRepository(repositoryId);
        InputStream artifactIs = null;
        List<String> extensions = Arrays.asList(".war", ".jar");

        for (String ext : extensions) {
            Response r = ClientBuilder
                    .newClient()
                    .target(repo.getUrl())
                    .path(gav.getGroupId().replace(".", "/"))
                    .path(gav.getArtifactId())
                    .path(gav.getVersion())
                    .path(String.format("%s-%s%s", gav.getArtifactId(), gav.getVersion(), ext))
                    .request()
                    .get();

            if (r.getStatus() == 200) {
                artifactIs = r.readEntity(InputStream.class);
                break;
            }
        }
        
        return artifactIs;
    }

    public InputStream getArtifactByDeploymentName(String deploymentName) throws FileNotFoundException {
        URI artifactLocation;

        for (Service s : getServices()) {
            for (ServiceVersion sv : s.getVersions()) {
                for (Deployment d : sv.getDeployments()) {
                    if (d.getName().equals(deploymentName)) {
                        if (ARTIFACT_LOCATION_TYPE_DISK.equals(sv.getArtifactLocationType())) {
                            artifactLocation = sv.getArtifactLocation();
                            return readArtifactFromDisk(artifactLocation);
                        } else if (ARTIFACT_LOCATION_TYPE_MAVEN_REPOSITORY.equals(sv.getArtifactLocationType())) {

                            return readArtifactFromMvnRepo(sv.getRepositoryId(), sv.getGav());
                        }
                    }
                }
            }
        }

        return null;
    }

    public InputStream getArtifactByLibraryId(String libraryId) throws FileNotFoundException {
        Library lib = getLibrary(libraryId);
        if (ARTIFACT_LOCATION_TYPE_DISK.equals(lib.getArtifactLocationType())) {

            return readArtifactFromDisk(lib.getArtifactLocation());
        } else if (ARTIFACT_LOCATION_TYPE_MAVEN_REPOSITORY.equals(lib.getArtifactLocationType())) {

            return readArtifactFromMvnRepo(lib.getRepositoryId(), lib.getGav());
        } else {
            return null;
        }

    }

    public List<DeploymentInstance> getDeploymentsForServer(String server) {
        List<DeploymentInstance> list = new ArrayList<>();

        for (Service s : getServices()) {
            for (ServiceVersion sv : s.getVersions()) {
                for (Deployment d : sv.getDeployments()) {
                    if (d.getServerDeployments().stream().filter(u -> u.getRequestedStatus()).filter(u -> u.getServerId().equals(server)).count() > 0) {
                        DeploymentInstance di = new DeploymentInstance();
                        di.setArtifactLocation(sv.getArtifactLocation());
                        di.setEnableHz(d.getEnableHz());
                        di.setHttpPort(d.getHttpPort());
                        di.setHttpsPort(d.getHttpsPort());
                        di.setInitialHeapSize(d.getInitialHeapSize());
                        di.setMaxHeapSize(d.getMaxHeapSize());
                        di.setHzConfiguration(d.getHzConfiguration());
                        di.setLibraryIds(sv.getLibraryIds());
                        di.setName(d.getName());
                        di.setPingPath(d.getPingPath());
                        di.setServerDeployments(d.getServerDeployments());
                        di.setServers(d.getServerDeployments().stream().map(sd -> getServer(sd.getServerId())).collect(Collectors.toList()));
                        di.setServiceName(s.getName());
                        di.setServiceVersionName(s.getName());
                        di.setServiceProperties(d.getServiceProperties());
                        di.setJdbcConnections(d.getJdbcConnections());

                        list.add(di);
                    }
                }
            }

        }

        return list;
        //Server s = servers.get(server);
        //return deployments.values().stream().filter(d -> d.getServers().contains(s)).collect(Collectors.toList());
    }

    public boolean setServiceVersionArtifact(String serviceId, String serviceVersionName, byte[] artifact) {

        Service s = getService(serviceId);
        AtomicBoolean result = new AtomicBoolean(false);

        s.getVersions().stream().filter(sv -> sv.getName().equals(serviceVersionName)).findFirst().ifPresent(sv -> {
            Path p = Paths.get("/tmp", String.format("%s-%s.war", serviceId, serviceVersionName));
            sv.setArtifactLocation(p.toUri());
            try {
                Files.write(p, artifact);
                ds.save(s);
                result.set(true);
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Failed to save service version artifact", ex);
                result.set(false);
            }
        });

        return result.get();

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

    public List<MavenRepository> getMavenRepositories() {
        return ds.find(MavenRepository.class).asList();
    }

    public MavenRepository getMavenRepository(String id) {
        return getMavenRepository(new ObjectId(id));
    }

    public MavenRepository getMavenRepository(ObjectId id) {
        return ds.find(MavenRepository.class).filter("id == ", id).get();
    }

    public MavenRepository addMavenRepository(MavenRepository s) {
        Key<MavenRepository> k = ds.save(s);
        LOG.info(k.getId().getClass().getName());
        return getMavenRepository((ObjectId) k.getId());
    }

    public Library updateLibrary(Library s) {
        ds.save(s);

        return getLibrary(s.getId());
    }

}
