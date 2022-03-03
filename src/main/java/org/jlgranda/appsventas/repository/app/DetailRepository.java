/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.Detail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface DetailRepository extends CrudRepository<Detail, Long> {

    @Query("select p from Detail p where p.invoiceId = :#{#invoiceId} order by p.unit ASC")
    public List<Detail> encontrarPorInvoiceId(Long invoiceId);

}
