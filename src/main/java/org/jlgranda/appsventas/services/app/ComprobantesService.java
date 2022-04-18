/*
 * Copyright (C) 2022 jlgranda
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.services.app;

import com.rolandopalermo.facturacion.ec.domain.Invoice;
import java.util.Optional;
import org.jlgranda.appsventas.repository.app.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlgranda
 */
@Service
public class ComprobantesService {

    @Autowired
    InvoiceRepository repository;

    public InvoiceRepository getRepository() {
        return repository;
    }

    public Iterable<Invoice> findAllBySupplierId(String supplierId) {
        return this.getRepository().findBySupplierId(supplierId);
    }

    /**
     * Devolver la instancia <tt>Invoice</tt> para el access_key dado como
     * parámentro, discriminando el campo eliminado
     *
     * @param claveAcceso
     * @return
     */
    public Optional<Invoice> encontrarPorClaveAcceso(String claveAcceso) {
        return this.getRepository().encontrarPorAccessKey(claveAcceso);
    }

    public Invoice guardar(Invoice invoice) {
        return this.getRepository().save(invoice);
    }

}
