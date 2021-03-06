/*
 * Copyright (C) 2022 usuario
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
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.app.CuentaBancaria;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface CuentaBancariaRepository extends CrudRepository<CuentaBancaria, Long> {

    @Query("select p from CuentaBancaria p where p.deleted = false and p.id = :#{#id}")
    public Optional<CuentaBancaria> encontrarPorId(Long id);

    @Query("select p from CuentaBancaria p where p.deleted = false and p.code = :#{#code}")
    public Optional<CuentaBancaria> encontrarPorCode(String code);

    @Query("select p from CuentaBancaria p where p.deleted = false and p.uuid = :#{#uuid}")
    public Optional<CuentaBancaria> encontrarPorUuid(String uuid);

    @Query("select p from CuentaBancaria p where p.deleted = false and p.organizacionId = :#{#organizacionId} order by p.name ASC")
    public List<CuentaBancaria> encontrarPorOrganizacionId(Long organizacionId);

}
