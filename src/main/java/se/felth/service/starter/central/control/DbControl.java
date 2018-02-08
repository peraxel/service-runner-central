/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.control;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

/**
 *
 * @author pa
 */
@Singleton
//@Startup
public class DbControl {

    private static final Logger LOG = Logger.getLogger(DbControl.class.getName());

    MongoClient client = new MongoClient();
    private Morphia morphia;
    private Datastore datastore;

    @PostConstruct
    public void init() {
        if (client != null) {
            LOG.info("MongoDB client was not null");
        }
        client = new MongoClient();
        morphia = new Morphia();
        datastore = morphia.createDatastore(client, "ssc");

    }

    @Produces
    public MongoDatabase getDatabase() {
        return client.getDatabase("ssc");
    }

    @Produces
    public Datastore getDatastore() {
        return datastore;
    }

    @PreDestroy
    public void destroy() {
        LOG.info("Running DbControl @PreDestroy");
        
        
        client.close();
        
        LOG.info("Client closed in @PreDestroy");
    }
}
