/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.net.URI;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author pa
 */
public class Library {

    @Id
    ObjectId id;
    String name;
    String artifactLocationType;
    String repositoryId;
    Gav gav;
    URI artifactLocation;

    public String getId() {
        return id == null ? null : id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getArtifactLocationType() {
        return artifactLocationType;
    }

    public void setArtifactLocationType(String artifactLocationType) {
        this.artifactLocationType = artifactLocationType;
    }

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public Gav getGav() {
        return gav;
    }

    public void setGav(Gav gav) {
        this.gav = gav;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getArtifactLocation() {
        return artifactLocation;
    }

    public void setArtifactLocation(URI artifactLocation) {
        this.artifactLocation = artifactLocation;
    }

}
