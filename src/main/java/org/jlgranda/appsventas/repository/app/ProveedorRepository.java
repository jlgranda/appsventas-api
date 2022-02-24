/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.Proveedor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface ProveedorRepository extends CrudRepository<Proveedor, Long> {
    
//    @Query("")
//    public List<Proveedor> encontrarPorOrganizacionId();
}
