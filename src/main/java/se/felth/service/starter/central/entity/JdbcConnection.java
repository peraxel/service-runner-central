/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

import java.util.Map;

/**
 *
 * @author pa
 */
public class JdbcConnection {
	String poolName;
	String jndiName;
	String datasourceClassname;
	Map<String,String> properties;

	public String getPoolName() {
		return poolName;
	}

	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public String getJndiName() {
		return jndiName;
	}

	public void setJndiName(String jndiName) {
		this.jndiName = jndiName;
	}

	public String getDatasourceClassname() {
		return datasourceClassname;
	}

	public void setDatasourceClassname(String datasourceClassname) {
		this.datasourceClassname = datasourceClassname;
	}

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}
	
	
}
