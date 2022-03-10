/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.Optional;
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
    private SubjectService subjectService;
    
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
    /**
     * Devolver las instancias <tt>Organization</tt> para el owner dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param ownerId
     * @return
     */
    public List<Organization> encontrarPorOwnerId(Long ownerId) {
        return this.getRepository().encontrarPorOwnerId(ownerId);
    }
    
    /**
     * Devolver las instancia <tt>Organization</tt> para el ruc dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param ruc
     * @return
     */
    public Optional<Organization> encontrarPorRuc(String ruc) {
        return this.getRepository().findByRuc(ruc);
    }
    
    /**
     *
     * @param userId
     * @return
     */
    public Organization encontrarPorSubjectId(Long userId) {
        List<Organization> organization = this.encontrarPorOwnerId(userId);
        if (!organization.isEmpty()) {
            return organization.get(0);
        } else { //Es posible que la organizacin tenga el mismo RUC que el usuario
            Optional<Subject> subjectOpt = subjectService.encontrarPorId(userId);
            if ( subjectOpt.isPresent() ){
                Optional<Organization> organizationOpt = this.encontrarPorRuc(subjectOpt.get().getRuc());
                if ( organizationOpt.isPresent() ){
                    return organizationOpt.get();
                } else {
                    //Crear la organización si el RUC es valido
                }
            }
        }
        return null; //No existe organización alguna
    }
}
