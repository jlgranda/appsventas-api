/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.Payment;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface PaymentRepository extends CrudRepository<Payment, Long> {

    @Query("select p from Payment p where p.invoiceId = :#{#invoiceId} order by p.datePaymentCancel DESC")
    public List<Payment> encontrarPorInvoiceId(Long invoiceId);

}
