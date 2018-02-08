/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.felth.service.starter.central.entity;

/**
 *
 * @author jnj112
 */
public class ServerDeployment {
    String serverId;
    Boolean requestedStatus;
    Boolean actualStatus;

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public Boolean getRequestedStatus() {
        return requestedStatus;
    }

    public void setRequestedStatus(Boolean requestedStatus) {
        this.requestedStatus = requestedStatus;
    }

    public Boolean getActualStatus() {
        return actualStatus;
    }

    public void setActualStatus(Boolean actualStatus) {
        this.actualStatus = actualStatus;
    }
    
    
}
