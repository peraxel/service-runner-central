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
public class HazelcastConfiguration {
    int port;
    boolean enableQuorum;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isEnableQuorum() {
        return enableQuorum;
    }

    public void setEnableQuorum(boolean enableQuorum) {
        this.enableQuorum = enableQuorum;
    }
    
    
    
}
