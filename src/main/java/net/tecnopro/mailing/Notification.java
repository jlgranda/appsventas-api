/*
 * Copyright (C) 2016 jlgranda
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package net.tecnopro.mailing;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.jlgranda.appsventas.domain.PersistentObject;

/**
 * Configuración de noticiación
 * 
 * @author jlgranda
 */
@Entity
@Table(name = "Notification")
public class Notification extends PersistentObject<Notification> implements Serializable {
    
    private static final long serialVersionUID = -6144539230859311902L;
    
    @Column(nullable = true, length = 1024)
    private String ffrom;
    @Column(nullable = true, length = 1024)
    private String tto;
    @Column(nullable = true, length = 1024)
    private String bcc;
    @Column(nullable = true, length = 1024)
    private String cc;
    
    private Long messageId;
    
    private boolean flag;
    
    @Column(name="sri_notificacion", nullable = true)
    private boolean sriNotificacion = false;
    
    @Column(name="sri_clave_acceso", nullable = true)
    private String sriClaveAcceso;
    
    @Column(name="sri_pdf", length = 1024)
    @Basic(fetch = FetchType.LAZY)
    private byte[] sriPDF;
    
    @Column(name="sri_xml", length = 1024)
    @Basic(fetch = FetchType.LAZY)
    private byte[] sriXML;
    
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    protected Date sendOn;

    public String getFfrom() {
        return ffrom;
    }

    public void setFfrom(String ffrom) {
        this.ffrom = ffrom;
    }

    public String getTto() {
        return tto;
    }

    public void setTto(String tto) {
        this.tto = tto;
    }
    
    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public Date getSendOn() {
        return sendOn;
    }

    public void setSendOn(Date sendOn) {
        this.sendOn = sendOn;
    }

    public boolean isSriNotificacion() {
        return sriNotificacion;
    }

    public void setSriNotificacion(boolean sriNotificacion) {
        this.sriNotificacion = sriNotificacion;
    }

    public String getSriClaveAcceso() {
        return sriClaveAcceso;
    }

    public void setSriClaveAcceso(String sriClaveAcceso) {
        this.sriClaveAcceso = sriClaveAcceso;
    }

    public byte[] getSriPDF() {
        return sriPDF;
    }

    public void setSriPDF(byte[] sriPDF) {
        this.sriPDF = sriPDF;
    }

    
    public byte[] getSriXML() {
        return sriXML;
    }

    public void setSriXML(byte[] sriXML) {
        this.sriXML = sriXML;
    }
    
}
