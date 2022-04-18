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
package org.jlgranda.appsventas.repository.app;

import com.rolandopalermo.facturacion.ec.domain.Invoice;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repositorio de entidades Invoice del SRI
 *
 * @author jlgranda
 */
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Query("select i.accessKey from Invoice i where i.supplierId = ?1 and i.isDeleted = ?2")
    List<String> findBySupplierIdAndIsDeleted(String supplierId, boolean isDeleted);

    @Query("select i from Invoice i where i.supplierId = ?1 and i.isDeleted = false")
    List<Invoice> findBySupplierId(String supplierId);

    @Query("select p from Invoice p where p.accessKey = :#{#accessKey}")
    public Optional<Invoice> encontrarPorAccessKey(String accessKey);

}
