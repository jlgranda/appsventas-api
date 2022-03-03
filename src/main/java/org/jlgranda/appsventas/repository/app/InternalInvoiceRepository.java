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
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author jlgranda
 */
public interface InternalInvoiceRepository extends CrudRepository<InternalInvoice, Long> {

    @Query("select p from InternalInvoice p where p.deleted = false and p.organizacionId = :#{#organizacionId} order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorOrganizacionId(Long organizacionId);
    
    @Query("select p from InternalInvoice p where p.deleted = false and p.organizacionId = :#{#organizacionId} and p.documentType = :#{#documentType} order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorOrganizacionIdYDocumentType(Long organizacionId, DocumentType documentType);
}