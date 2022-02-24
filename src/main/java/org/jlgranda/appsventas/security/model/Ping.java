/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.security.model;

/**
 *
 * @author jlgranda
 */
public class Ping {
    
    String version;
    String messages;

    public Ping(String version, String messages) {
        this.version = version;
        this.messages = messages;
    }
    
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessages() {
        return messages;
    }

    public void setMessages(String messages) {
        this.messages = messages;
    }
    
    
}
