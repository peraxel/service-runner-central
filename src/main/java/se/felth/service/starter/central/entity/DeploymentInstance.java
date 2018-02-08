/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.net.URI;
import java.util.List;

/**
 *
 * @author pa
 */
public class DeploymentInstance extends Deployment {

    String serviceName;
    String serviceVersionName;
    URI artifactLocation;
    List<String> libraryIds;
    List<Server> servers;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceVersionName() {
        return serviceVersionName;
    }

    public void setServiceVersionName(String serviceVersionName) {
        this.serviceVersionName = serviceVersionName;
    }

    public URI getArtifactLocation() {
        return artifactLocation;
    }

    public void setArtifactLocation(URI artifactLocation) {
        this.artifactLocation = artifactLocation;
    }

    public List<String> getLibraryIds() {
        return libraryIds;
    }

    public void setLibraryIds(List<String> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public List<Server> getServers() {
        return servers;
    }

    public void setServers(List<Server> servers) {
        this.servers = servers;
    }

}
