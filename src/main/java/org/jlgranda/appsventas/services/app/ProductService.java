/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.Product;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.jlgranda.appsventas.dto.app.ProductData;
import org.jlgranda.appsventas.repository.app.ProductRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public ProductRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Product</tt> para la organizacionId dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param organizacionId
     * @return
     */
    public List<Product> encontrarPorOrganizacionId(Long organizacionId) {
        return this.getRepository().encontrarPorOrganizacionId(organizacionId);
    }

    /**
     * Devolver las instancias <tt>Product</tt> para la organizacionId y tipo de
     * producto dados como parámentros, discriminando el campo eliminado
     *
     * @param organizacionId
     * @param productType
     * @return
     */
    public List<Product> encontrarPorOrganizacionIdYProductType(Long organizacionId, ProductType productType) {
        return this.getRepository().encontrarPorOrganizacionIdYProductType(organizacionId, productType);
    }

    public ProductData buildProductData(Product p) {
        ProductData productData = new ProductData();
        BeanUtils.copyProperties(p, productData);
        return productData;
    }

}
