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
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.CuentaBancaria;
import org.jlgranda.appsventas.dto.app.CuentaBancariaData;
import org.jlgranda.appsventas.repository.app.CuentaBancariaRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class CuentaBancariaService {

    @Autowired
    private CuentaBancariaRepository repository;

    public CuentaBancariaRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>CuentaBancaria</tt> para el id dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<CuentaBancaria> encontrarPorId(Long id) {
        return this.getRepository().encontrarPorId(id);
    }

    /**
     * Devolver las instancias <tt>CuentaBancaria</tt> para el code dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param code
     * @return
     */
    public Optional<CuentaBancaria> encontrarPorCode(String code) {
        return this.getRepository().encontrarPorCode(code);
    }

    /**
     * Devolver las instancias <tt>CuentaBancaria</tt> para el uuid dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<CuentaBancaria> encontrarPorUuid(String uuid) {
        return this.getRepository().encontrarPorUuid(uuid);
    }

    /**
     * Devolver las instancias <tt>CuentaBancaria</tt> para la organizacionId
     * dado como parámentro, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<CuentaBancaria> encontrarPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarPorOrganizacionId(organizacionId);
    }

    public CuentaBancariaData buildCuentaBancariaData(CuentaBancaria cb) {
        CuentaBancariaData cuentaBancariaData = new CuentaBancariaData();
        BeanUtils.copyProperties(cb, cuentaBancariaData);
        return cuentaBancariaData;
    }

    public CuentaBancaria crearInstancia() {
        CuentaBancaria _instance = new CuentaBancaria();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        return _instance;
    }

    public CuentaBancaria crearInstancia(Subject user) {
        CuentaBancaria _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public CuentaBancaria guardar(CuentaBancaria cuentaBancaria) {
        return this.getRepository().save(cuentaBancaria);
    }

}
