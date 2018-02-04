/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author pa
 */

public class Deployment {
	String name;
	List<String> serverIds;
	String pingPath;
	Boolean enableHz;
	Integer httpPort;
	Integer httpsPort;
	Integer hzPort;
	List<JdbcConnection> jdbcConnections;
	ServiceProperties serviceProperties;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getServerIds() {
		return serverIds == null ? Arrays.asList() : serverIds;
	}

	public void setServerIds(List<String> serverIds) {
		this.serverIds = serverIds;
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

	public Integer getHzPort() {
		return hzPort;
	}

	public void setHzPort(Integer hzPort) {
		this.hzPort = hzPort;
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
