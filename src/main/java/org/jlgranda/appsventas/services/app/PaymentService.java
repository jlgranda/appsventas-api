/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.Subject;
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
    
    public Payment crearInstancia() {
        Payment _instance = new Payment();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        _instance.setUuid(UUID.randomUUID().toString());
        _instance.setDiscount(BigDecimal.ZERO);
        _instance.setChange(BigDecimal.ZERO);
        _instance.setMethod("EFECTIVO");
        return _instance;
    }
    
    public Payment crearInstancia(Subject user) {
        Payment _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }
    
    public Payment guardar(Payment payment) {
        return this.getRepository().save(payment);
    }
    
    public Iterable<Payment> guardarLista(List<Payment> payments) {
        return this.getRepository().saveAll(payments);
    }
    
}
