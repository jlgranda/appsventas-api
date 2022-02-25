/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Organization;
import org.jlgranda.appsventas.repository.app.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class OrganizationService {

    @Autowired
    private OrganizationRepository repository;

    public OrganizationRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Organization</tt> para el owner dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param owner
     * @return
     */
    public List<Organization> encontrarPorOwner(Subject owner) {
        return this.getRepository().encontrarPorOwner(owner);
    }
}
