/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import com.rolandopalermo.facturacion.ec.domain.Supplier;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface ProveedorRepository extends CrudRepository<Supplier, Long> {
    
//    @Query("")
//    public List<Proveedor> encontrarPorOrganizacionId();
}
