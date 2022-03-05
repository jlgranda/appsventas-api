/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Organization;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface OrganizationRepository extends CrudRepository<Organization, Long> {
    
    Optional<Organization> findByRuc(String ruc);

    /**
     *
     * @param owner
     * @return
     */
    @Query("select p from Organization p where p.owner = :#{#owner}")
    public List<Organization> encontrarPorOwner(Subject owner);
    
    /**
     *
     * @param ownerId
     * @return
     */
    @Query("select p from Organization p where p.owner.id = :#{#ownerId}")
    public List<Organization> encontrarPorOwnerId(Long ownerId);

}
