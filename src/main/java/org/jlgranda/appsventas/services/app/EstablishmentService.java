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
import org.jlgranda.appsventas.domain.app.Establishment;
import org.jlgranda.appsventas.dto.app.EmissionPointData;
import org.jlgranda.appsventas.dto.app.EstablishmentData;
import org.jlgranda.appsventas.repository.app.EstablishmentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class EstablishmentService {

    @Autowired
    private EstablishmentRepository repository;

    public EstablishmentRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Establishment</tt> para el id dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<Establishment> encontrarPorId(Long id) {
        return this.getRepository().encontrarPorId(id);
    }

    /**
     * Devolver las instancias <tt>Establishment</tt> para la organizacionId
     * dado como parámentro, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<Establishment> encontrarPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarPorOrganizacionId(organizacionId);
    }
    
    public EstablishmentData buildEstablishment(Establishment est) {
        EstablishmentData establishmentData = new EstablishmentData();
        BeanUtils.copyProperties(est, establishmentData);
        return establishmentData;
    }
    
    public EstablishmentData buildEstablishment(Establishment est, List<EmissionPointData>emisionPointsData) {
        EstablishmentData establishmentData = new EstablishmentData();
        BeanUtils.copyProperties(est, establishmentData);
        establishmentData.setEmisionPointsData(emisionPointsData);
        return establishmentData;
    }

}
