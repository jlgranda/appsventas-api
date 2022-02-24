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

import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Constantes;

@MappedSuperclass
public abstract class DeletableObject<E extends DeletableObject<E>> extends PersistentObject<E>
{
   private static final long serialVersionUID = 49008810086044438L;

   @Column
   private boolean deleted = false;

   @Temporal(TemporalType.TIMESTAMP)
   @Column(name = "deletedOn")
   private Date deletedOn;

   @Override
   @PrePersist
   void prePersist()
   {
      if (isDeleted() && (getDeletedOn() == null))
      {
         setDeletedOn(new Date());
      }
      super.prePersist();
   }

   @Override
   void preUpdate()
   {
      if (isDeleted() && (getDeletedOn() == null)) {
           setDeletedOn(new Date());
           setStatus(StatusType.INACTIVE.toString());
           setName(getName().concat(Constantes.ESTADO_ELIMINADO).concat("" + Dates.now().getTime()));
           if ( Strings.isNullOrEmpty(getCode()) && Strings.isNullOrEmpty(getUuid()) ){ //corregir
               this.uuid = UUID.randomUUID().toString();
               this.code = this.uuid;
           }
           setCode(getCode().concat(Constantes.ESTADO_ELIMINADO).concat("" + Dates.now().getTime()));
       }
      super.preUpdate();
   }

   public boolean isDeleted()
   {
      return deleted;
   }

   public void setDeleted(final boolean deleted)
   {
      this.deleted = deleted;
   }

   public Date getDeletedOn()
   {
      return deletedOn;
   }

   public void setDeletedOn(final Date deletedOn)
   {
      this.deletedOn = deletedOn;
   }
}
