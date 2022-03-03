/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.Payment;
import org.jlgranda.appsventas.dto.app.PaymentData;
import org.jlgranda.appsventas.repository.app.PaymentRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository repository;

    public PaymentRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Payment</tt> para el invoiceId dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param invoiceId
     * @return
     */
    public List<Payment> encontrarPorInvoiceId(Long invoiceId) {
        return this.getRepository().encontrarPorInvoiceId(invoiceId);
    }

    public PaymentData buildPaymentData(Payment pay) {
        PaymentData paymentData = new PaymentData();
        BeanUtils.copyProperties(pay, paymentData);
        return paymentData;
    }

}
