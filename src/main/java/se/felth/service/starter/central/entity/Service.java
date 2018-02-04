/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;

/**
 *
 * @author pa
 */
public class Service {
	@Id
	ObjectId id;
	String name;
	List<ServiceVersion> versions = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ServiceVersion> getVersions() {
		return versions == null ? Arrays.asList() : versions;
	}

	public void setVersions(List<ServiceVersion> versions) {
		this.versions = versions;
	}

	public String getId() {
		return id.toString();
	}

	public void setId(ObjectId id) {
		this.id = id;
	}
	
	
	
}
