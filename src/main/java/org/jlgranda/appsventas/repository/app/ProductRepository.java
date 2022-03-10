/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.app.Product;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Query("select p from Product p where p.deleted = false and p.id = :#{#id}")
    public Optional<Product> encontrarPorId(Long id);

    @Query("select p from Product p where p.deleted = false and p.organizacionId = :#{#organizacionId} order by p.name ASC")
    public List<Product> encontrarPorOrganizacionId(Long organizacionId);

    @Query("select p from Product p where p.deleted = false and p.organizacionId = :#{#organizacionId} "
            + "and p.productType = :#{#productType} order by p.name ASC")
    public List<Product> encontrarPorOrganizacionIdYProductType(Long organizacionId, ProductType productType);

}
