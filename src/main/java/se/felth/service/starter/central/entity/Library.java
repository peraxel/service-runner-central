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
	URI artifactLocation;

	public String getId() {
		return id == null ? null : id.toString();
	}

	public void setId(ObjectId id) {
		this.id = id;
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
