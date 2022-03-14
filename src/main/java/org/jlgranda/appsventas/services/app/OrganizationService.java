/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.Optional;
import net.tecnopro.util.Dates;
import net.tecnopro.util.Strings;
import org.jlgranda.appsventas.Constantes;
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
        boolean actualizar = false;
        List<Organization> organizations = this.encontrarPorOwnerId(userId);
        if (!organizations.isEmpty()) {
            return organizations.get(0);
        } else { //Es posible que la organizacin tenga el mismo RUC que el usuario
            Optional<Subject> subjectOpt = subjectService.encontrarPorId(userId);
            if (subjectOpt.isPresent()) {
                Subject subject = subjectOpt.get();
                if (Strings.isNullOrEmpty(subject.getRuc())) {
                    subject.setRuc(subject.getCode()); //Igualar el RUC con el código si es valido
                    actualizar = true;
                }

                if (actualizar) {
                    subjectService.guardar(subject); //Enviar los cambios en subject
                }
                if (Strings.validateTaxpayerDocument(subject.getRuc())) {
                    Optional<Organization> organizationOpt = this.encontrarPorRuc(subject.getRuc());
                    if (organizationOpt.isPresent()) {
                        return organizationOpt.get();
                    } else {

                        Organization organization = new Organization();
                        organization.setName(subject.getInitials());
                        organization.setRuc(subject.getRuc());
                        organization.setInitials(organization.getName().toUpperCase());
                        organization.setDireccion(subject.getDescription());
                        organization.setOrganizationType(Organization.Type.NATURAL);
                        organization.setDescription(Constantes.CREADO_POR + Constantes.ESPACIO + Constantes.FAZIL);
                        organization.setOwner(subject);
                        organization.setAuthor(subject);
                        organization.setLastUpdate(Dates.now());

                        this.getRepository().save(organization); //Crear organización
                        return organization;
                    }
                }

            }
        }
        return null; //No existe organización alguna
    }

    public Organization guardar(Organization organization) {
        return this.getRepository().save(organization);
    }
}
