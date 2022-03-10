/*
 * Copyright (C) 2022 jlgranda
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;

/**
 *
 * @author jlgranda
 */
@Entity
@Table(name = "invoice_cronos", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InvoiceCronos extends DeletableObject<InvoiceCronos> implements Serializable { 
    
    /**
     * Organización de la factura recurrente
     */
    @Column(name = "organization_id", insertable = true, updatable = true, nullable = true)
    private Long organizacionId;
    
    /**
     * Factura base de recurrencia
     */
    @Column(name = "invoice_id", insertable = true, updatable = true, nullable = true)
    private Long invoiceId;
    
    /**
     * Período de nueva emisión
     */
    @Column(name = "periodo", insertable = true, updatable = true, nullable = true)
    private Long periodo;
    
    /**
     * Emitir sólo en días laborables
     */
    @Column(name = "solo_dias_laborables", insertable = true, updatable = true, nullable = true)
    private Boolean soloDiasLaborables;
    
}
