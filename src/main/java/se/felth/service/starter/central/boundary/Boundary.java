/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.boundary;

import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.internal.org.bouncycastle.util.io.pem.PemObject;
import com.auth0.jwt.internal.org.bouncycastle.util.io.pem.PemReader;
import com.auth0.jwt.internal.org.bouncycastle.util.io.pem.PemWriter;
import static com.auth0.jwt.pem.PemWriter.writePrivateKey;
import static com.auth0.jwt.pem.PemWriter.writePublicKey;
import com.mongodb.client.MongoDatabase;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
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
import javax.json.Json;
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
import se.felth.service.starter.central.entity.PemKeyPair;
import se.felth.service.starter.central.entity.ServerDeployment;
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

    public PemKeyPair createKeyPair() throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        // create key pair
        final KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        final KeyPair keyPair = generator.generateKeyPair();
        // write private key
        final RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        StringWriter privateSw = new StringWriter();
        PemWriter privateWriter = new PemWriter(privateSw);
        privateWriter.writeObject(new PemObject("RSA PRIVATE KEY", privateKey.getEncoded()));
        privateWriter.close();
        
        // write public key
        final RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        StringWriter publicSw = new StringWriter();
        PemWriter publicWriter = new PemWriter(publicSw);
        publicWriter.writeObject(new PemObject("RSA PUBLIC KEY", publicKey.getEncoded()));
        publicWriter.close();
        
        return new PemKeyPair(publicSw.toString(), privateSw.toString());
    }

    public Server addServer(Server s) {
        PemKeyPair kp;
        try {
            kp = createKeyPair();
            s.setPublicKey(kp.getPublicKey());
        } catch (NoSuchAlgorithmException | NoSuchProviderException | IOException ex) {
            Logger.getLogger(Boundary.class.getName()).log(Level.SEVERE, null, ex);
            throw new RuntimeException("Failed to create keys", ex);
        }

        Key<Server> k = ds.save(s);

        LOG.info(k.getId().getClass().getName());
        s = getServer((String) k.getId());
        s.setPrivateKey(kp.getPrivateKey());

        return s;
    }

    public String serverAuth(String token) {
        Decoder dec = Base64.getDecoder();
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        
        String[] tokenParts = token.split("\\.");
        String iss = Json.createReader(new StringReader(new String(dec.decode(tokenParts[1])))).readObject().getString("iss");
        Server s = getServer(iss);
        //new PemReader(new StringReader(s.getPublicKey())).readPemObject().
        

        Map<String, Object> claims;
        try {
            JWTVerifier verifier = new JWTVerifier(getPemPublicKey(s.getPublicKey()));
            claims = verifier.verify(token);
            LOG.info(claims.toString());
            //return new UserInfo(claims.get("sub").toString(), null, null, null, null, null);
            return (String)claims.get("iss");
        } catch (Exception e) {
            LOG.log(Level.WARNING, "Token validation failed", e);
            return null;
        }
    }
    

    
    public PublicKey getPemPublicKey(String pem) throws Exception {
        String publicKeyPEM = pem.replace("-----BEGIN PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----BEGIN RSA PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("-----END RSA PUBLIC KEY-----", "");
        publicKeyPEM = publicKeyPEM.replace("\n", "");

        Decoder dec = Base64.getDecoder();
        byte[] decoded = dec.decode(publicKeyPEM);

        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
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

    public InputStream getArtifactByDeploymentName(String deploymentName, String serverId) throws FileNotFoundException {
        URI artifactLocation;

        for (Service s : getServices()) {
            for (ServiceVersion sv : s.getVersions()) {
                for (Deployment d : sv.getDeployments()) {
                    // Check if deployment name matches input and input serverId is in list of servers that should run this deployment
                    if (d.getName().equals(deploymentName) && d.getServerDeployments().stream().map(sd -> sd.getServerId()).anyMatch(sd -> sd.equals(serverId))) {
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

    public ServerDeployment setServerDeploymentActual(String deploymentName, String serverId, boolean status) {
        for (Service s : getServices()) {
            for (ServiceVersion sv : s.getVersions()) {
                for (Deployment d : sv.getDeployments()) {
                    if (d.getServerDeployments() != null) {
                        for(ServerDeployment sd : d.getServerDeployments()) {
                            if (sd.getServerId().equals(serverId)) {
                                sd.setActualStatus(status);
                                updateService(s);
                                return sd;
                            }
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    public List<DeploymentInstance> getDeploymentsForServer(String server) {
        List<DeploymentInstance> list = new ArrayList<>();

        for (Service s : getServices()) {
            for (ServiceVersion sv : s.getVersions()) {
                for (Deployment d : sv.getDeployments()) {
                    if (d.getServerDeployments() != null && d.getServerDeployments().stream().filter(u -> u.getRequestedStatus()).filter(u -> u.getServerId().equals(server)).count() > 0) {
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
