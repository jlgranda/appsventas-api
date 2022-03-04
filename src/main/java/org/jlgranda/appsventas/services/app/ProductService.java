/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Product;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.jlgranda.appsventas.domain.util.TaxType;
import org.jlgranda.appsventas.dto.UserData;
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

    public Product crearInstancia() {
        Product _instance = new Product();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setProductType(ProductType.RAW_MATERIAL);
        _instance.setTaxType(TaxType.IVA);
        return _instance;
    }

    public Product crearInstancia(Subject user) {
        Product _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public Product guardar(Product product) {
        return this.getRepository().save(product);
    }

}
