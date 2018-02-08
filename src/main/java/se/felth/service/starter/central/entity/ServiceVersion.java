/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author pa
 */
public class ServiceVersion {

    String name;
    String artifactLocationType;
    String repositoryId;
    Gav gav;

    @XmlTransient
    URI artifactLocation;
    List<String> libraryIds;
    List<Deployment> deployments;

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

    public List<String> getLibraryIds() {
        return libraryIds == null ? Arrays.asList() : libraryIds;
    }

    public void setLibraryIds(List<String> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public List<Deployment> getDeployments() {
        return deployments == null ? Arrays.asList() : deployments;
    }

    public void setDeployments(List<Deployment> deployments) {
        this.deployments = deployments;
    }

}
