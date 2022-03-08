/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.Detail;
import org.jlgranda.appsventas.dto.app.DetailData;
import org.jlgranda.appsventas.repository.app.DetailRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class DetailService {

    @Autowired
    private DetailRepository repository;

    public DetailRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Detail</tt> para el invoiceId dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param invoiceId
     * @return
     */
    public List<Detail> encontrarPorInvoiceId(Long invoiceId) {
        return this.getRepository().encontrarPorInvoiceId(invoiceId);
    }

    /**
     * Devolver el total de las instancias <tt>Detail</tt> para el invoiceId
     * dado como parámentro, discriminando el campo eliminado
     *
     * @param invoiceId
     * @return
     */
    public Optional<BigDecimal> encontrarTotalPorInvoiceId(Long invoiceId) {
        return this.getRepository().encontrarTotalPorInvoiceId(invoiceId);
    }

    public DetailData buildDetailData(Detail d) {
        DetailData detailData = new DetailData();
        BeanUtils.copyProperties(d, detailData);
        return detailData;
    }

    public Detail crearInstancia() {
        Detail _instance = new Detail();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        _instance.setAmount(BigDecimal.ZERO);
        _instance.setUnit(Constantes.INVOICE_UNIT);
        return _instance;
    }

    public Detail crearInstancia(Subject user) {
        Detail _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public Detail guardar(Detail detail) {
        return this.getRepository().save(detail);
    }

}
