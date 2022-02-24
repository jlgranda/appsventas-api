/*
 * Copyright (C) 2015 jlgranda
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
package org.jlgranda.appsventas.domain;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.Version;
import net.tecnopro.util.Strings;

/**
 *
 * @author jlgranda
 * @param <E>
 */
@MappedSuperclass
public abstract class BaseObject<E extends BaseObject<E>> implements Serializable {
    protected static final long serialVersionUID = -1272280183658745494L;

    protected static boolean getBooleanValue(final Boolean value) {
        return Boolean.valueOf(String.valueOf(value));
    }
    @Column(nullable = true)
    protected String uuid;
    @Column(nullable = true, unique = false)
    protected String code;
    @Enumerated(value = EnumType.STRING)
    @Column(nullable = true)
    protected CodeType codeType;
    @Column(nullable = true, length = 1024)
    protected String name;
    @Column(nullable = true, length = 4096)
    protected String description;
    @Version
    @Column(name = "version", nullable = false)
    protected int version = 0;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "lastUpdate", nullable = false)
    protected Date lastUpdate;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "createdOn", updatable = false, nullable = false)
    protected Date createdOn;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    protected Date activationTime;
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(nullable = true)
    protected Date expirationTime;
    @Column(nullable = true)
    protected Integer priority;
    @Column(nullable = true)
    protected Boolean active;
    @Column(nullable = true)
    protected String status;

    @Column(nullable = true)
    protected Short orden;
    
    @PrePersist
    void prePersist() {
        createdOn = new Date();
        active = true;
    }

    @PreUpdate
    void preUpdate() {
        if (Strings.isNullOrEmpty(this.uuid)){
            this.uuid = UUID.randomUUID().toString();
        }
        if (Strings.isNullOrEmpty(this.code)){
            this.code = this.uuid; //Asignar un código si no se ha definido nada
        }
        lastUpdate = new Date();
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    @SuppressWarnings(value = "unchecked")
    public E setLastUpdate(final Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return (E) this;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    @SuppressWarnings(value = "unchecked")
    public E setCreatedOn(final Date createdOn) {
        this.createdOn = createdOn;
        return (E) this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getActivationTime() {
        return activationTime;
    }

    public void setActivationTime(Date activationTime) {
        this.activationTime = activationTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getCode() {
        return code;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    
    public void setCode(String code) {
        this.code = code;
    }

    public CodeType getCodeType() {
        return codeType;
    }

    public void setCodeType(CodeType codeType) {
        this.codeType = codeType;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Transient
    public boolean isExpired() {
        Calendar now = Calendar.getInstance();
        return now.after(getExpirationTime());
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Short getOrden() {
        return orden;
    }

    public void setOrden(Short orden) {
        this.orden = orden;
    }
    
}
