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
import org.jlgranda.appsventas.domain.app.EmissionPoint;
import org.jlgranda.appsventas.dto.app.EmissionPointData;
import org.jlgranda.appsventas.repository.app.EmissionPointRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class EmissionPointService {

    @Autowired
    private EmissionPointRepository repository;

    public EmissionPointRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>EmissionPoint</tt> , discriminando el campo
     * eliminado
     *
     * @param id
     * @return
     */
    public List<EmissionPoint> encontrarActivos() {
        return this.getRepository().encontrarActivos();
    }

    /**
     * Devolver las instancias <tt>EmissionPoint</tt> , segun el campo
     * organizacionId de EmissionPoint discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<EmissionPoint> encontrarActivosPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarActivosPorOrganizacionId(organizacionId);
    }

    /**
     * Devolver las instancias <tt>EmissionPoint</tt> para el id dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<EmissionPoint> encontrarPorId(Long id) {
        return this.getRepository().encontrarPorId(id);
    }

    /**
     * Devolver las instancias <tt>EmissionPoint</tt> para el establishmentId
     * dado como parámentro, discriminando el campo eliminado
     *
     * @param establishmentId
     * @return
     */
    public List<EmissionPoint> encontrarPorEstablishmentId(Long establishmentId) {
        return this.getRepository().encontrarPorEstablishmentId(establishmentId);
    }

    public EmissionPointData buildEmissionPoint(EmissionPoint empt) {
        EmissionPointData emissionPointData = new EmissionPointData();
        BeanUtils.copyProperties(empt, emissionPointData);
        return emissionPointData;
    }

    public EmissionPoint crearInstancia() {
        EmissionPoint _instance = new EmissionPoint();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        return _instance;
    }

    public EmissionPoint crearInstancia(Subject user) {
        EmissionPoint _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public EmissionPoint guardar(EmissionPoint emissionPoint) {
        return this.getRepository().save(emissionPoint);
    }

    public Iterable<EmissionPoint> guardarLista(List<EmissionPoint> emissionPoints) {
        return this.getRepository().saveAll(emissionPoints);
    }

}
