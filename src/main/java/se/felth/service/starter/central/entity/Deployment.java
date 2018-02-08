/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author pa
 */
public class Deployment {

    String name;
    List<ServerDeployment> serverDeployments;
    String pingPath;
    Boolean enableHz;
    HazelcastConfiguration hzConfiguration;
    Integer httpPort;
    Integer httpsPort;
    Integer initialHeapSize;
    Integer maxHeapSize;
    List<JdbcConnection> jdbcConnections;
    ServiceProperties serviceProperties;
    Map<String,String> systemProperties;

    public Map<String, String> getSystemProperties() {
        return systemProperties;
    }

    public void setSystemProperties(Map<String, String> systemProperties) {
        this.systemProperties = systemProperties;
    }
    
    
    public HazelcastConfiguration getHzConfiguration() {
        return hzConfiguration;
    }

    public void setHzConfiguration(HazelcastConfiguration hzConfiguration) {
        this.hzConfiguration = hzConfiguration;
    }

    
    public Integer getInitialHeapSize() {
        return initialHeapSize;
    }

    public void setInitialHeapSize(Integer initialHeapSize) {
        this.initialHeapSize = initialHeapSize;
    }

    public Integer getMaxHeapSize() {
        return maxHeapSize;
    }

    public void setMaxHeapSize(Integer maxHeapSize) {
        this.maxHeapSize = maxHeapSize;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServerDeployment> getServerDeployments() {
        return serverDeployments;
    }

    public void setServerDeployments(List<ServerDeployment> servers) {
        this.serverDeployments = servers;
    }

    public String getPingPath() {
        return pingPath;
    }

    public void setPingPath(String pingPath) {
        this.pingPath = pingPath;
    }

    public Boolean getEnableHz() {
        return enableHz;
    }

    public void setEnableHz(Boolean enableHz) {
        this.enableHz = enableHz;
    }

    public Integer getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(Integer httpPort) {
        this.httpPort = httpPort;
    }

    public Integer getHttpsPort() {
        return httpsPort;
    }

    public void setHttpsPort(Integer httpsPort) {
        this.httpsPort = httpsPort;
    }

    public List<JdbcConnection> getJdbcConnections() {
        return jdbcConnections;
    }

    public void setJdbcConnections(List<JdbcConnection> jdbcConnections) {
        this.jdbcConnections = jdbcConnections;
    }

    public ServiceProperties getServiceProperties() {
        return serviceProperties;
    }

    public void setServiceProperties(ServiceProperties serviceProperties) {
        this.serviceProperties = serviceProperties;
    }

}
