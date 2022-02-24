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

import java.util.UUID;
import javax.persistence.Column;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Strings;


/**
 *
 * @author jlgranda
 * @param <E>
 */
@MappedSuperclass
public abstract class PersistentObject<E extends PersistentObject<E>> extends BaseObject<E> {

    private static final long serialVersionUID = 936139131004156038L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id = null;

    public Long getId() {
        return id;
    }
    
    @PrePersist
    @Override
    void prePersist() {
        this.createdOn = Dates.now();
        this.uuid = UUID.randomUUID().toString();
        this.status = StatusType.ACTIVE.toString();
        if (Strings.isNullOrEmpty(this.code)){
            this.code = this.uuid; //Asignar un código si no se ha definido nada
        }
        super.prePersist();
    }
   
        
    @ManyToOne(optional = true)
    @JoinColumn(name = "author", nullable = true)
    private Subject author;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "owner", nullable = true)
    private Subject owner;
    
    public boolean isPersistent() {
        return getId() != null;
    }

    @SuppressWarnings("unchecked")
    public E setId(final Long id) {
        if (this.id != null) {
            throw new IllegalStateException("Cannot alter immutable ID of persistent object with id: " + id);
        }
        this.id = id;
        return (E) this;
    }
    
    public Subject getAuthor() {
        return author;
    }

    public void setAuthor(Subject author) {
        this.author = author;
    }

    public Subject getOwner() {
        return owner;
    }

    public void setOwner(Subject owner) {
        this.owner = owner;
    }

    
    @Override
    public String toString() {
        //return String.valueOf(getId());
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }

}
