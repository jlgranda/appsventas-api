/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.app.Detail;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface DetailRepository extends CrudRepository<Detail, Long> {

    @Query("select p from Detail p where p.invoiceId = :#{#invoiceId} order by p.createdOn DESC")
    public List<Detail> encontrarPorInvoiceId(Long invoiceId);

    @Query("select sum(case iva12 when true then (p.price + p.price*0.12) else p.price end) "
            + "from Detail p where p.invoiceId = :#{#invoiceId}")
    public Optional<BigDecimal> encontrarTotalPorInvoiceId(Long invoiceId);

}
