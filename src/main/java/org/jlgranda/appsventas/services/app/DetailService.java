/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
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

    public DetailData buildDetailData(Detail d) {
        DetailData detailData = new DetailData();
        BeanUtils.copyProperties(d, detailData);
        return detailData;
    }

}
