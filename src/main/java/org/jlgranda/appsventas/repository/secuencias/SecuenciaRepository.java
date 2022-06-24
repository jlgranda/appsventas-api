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
package org.jlgranda.appsventas.repository.secuencias;

import java.util.Optional;
import org.jlgranda.appsventas.domain.secuencias.Secuencia;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jlgranda
 */
public interface SecuenciaRepository extends CrudRepository<Secuencia, Long> {
    
    @Query("select s from Secuencia s where s.deleted = false and s.ruc= :#{#ruc} and s.entidad = :#{#entidad} and s.estab = :#{#estab} and s.ptoEmi = :#{#ptoEmi}")
    public Optional<Secuencia> encontrarPorEntidadEstabPtoEmi(@Param("ruc") String ruc, @Param("entidad") String entidad, @Param("estab") String estab, @Param("ptoEmi") String ptoEmi);
    
    @Query("select s from Secuencia s where s.deleted = false and s.ruc= :#{#ruc} and s.entidad = :#{#entidad} and s.estab = :#{#estab} and s.ptoEmi = :#{#ptoEmi} and s.ambienteSRI = :#{#ambienteSRI}")
    public Optional<Secuencia> encontrarPorEntidadEstabPtoEmiAmbiente(@Param("ruc") String ruc, @Param("entidad") String entidad, @Param("estab") String estab, @Param("ptoEmi") String ptoEmi, @Param("ambienteSRI") String ambienteSRI);
    
}
