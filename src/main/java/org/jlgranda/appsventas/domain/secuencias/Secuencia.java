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
package org.jlgranda.appsventas.domain.secuencias;

/**
 *
 * @author jlgranda
 */

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;

@Entity
@Table(name = "Secuencia", schema = "public")
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class Secuencia extends DeletableObject<Secuencia> implements Serializable {
    
    /**
     * Tipo de comprobante o entidad de datos
     */
    @Column(name = "entidad")
    String entidad;
    
    /**
     * Establecimiento
     */
    @Column(name = "estab")
    String estab;
    
    /**
     * Punto de emisión
     */
    @Column(name = "pto_emi")
    String ptoEmi;
    
    @Column(name = "valor_actual")
    Long valorActual;
    
    @Column(name = "agregar_anio")
    private boolean agregarAnio = true;
    
    
    @Column(name = "digitos")
    private Long digitos;
    
}
