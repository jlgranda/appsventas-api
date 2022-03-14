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

import java.util.List;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.domain.app.InternalInvoice;
import org.jlgranda.appsventas.domain.app.view.InvoiceView;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jlgranda
 */
public interface InternalInvoiceRepository extends CrudRepository<InternalInvoice, Long> {

    @Query("select p from InternalInvoice p where p.deleted = false and p.author = :#{#author} and p.organizacionId = :#{#organizacionId} and p.documentType = :#{#documentType} and not p.claveAcceso is null order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorAuthorYOrganizacionIdYDocumentType(Subject author, Long organizacionId, DocumentType documentType);

    @Query("select p from InternalInvoice p where p.deleted = false and p.owner = :#{#owner} and p.organizacionId = :#{#organizacionId} and p.documentType = :#{#documentType} and not p.claveAcceso is null order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorOwnerYOrganizacionIdYDocumentType(Subject owner, Long organizacionId, DocumentType documentType);

    @Query("select p from InternalInvoice p where p.deleted = false and p.author = :#{#author} and p.documentType = :#{#documentType} and not p.claveAcceso is null order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorAuthorYDocumentType(Subject author, DocumentType documentType);

    @Query("select p from InternalInvoice p where p.deleted = false and p.owner = :#{#owner} and p.documentType = :#{#documentType} and not p.claveAcceso is null order by p.emissionOn DESC")
    public List<InternalInvoice> encontrarPorOwnerYDocumentType(Subject owner, DocumentType documentType);

    //Vistas
    @Query(nativeQuery = true, value = "select inv_.id as id, inv_.uuid as uuid, \n"
            + "upper(cliente.firstname || ' ' || cliente.surname) as customerFullName, \n"
            + "emisor.organization_name as subjectFullName, \n"
            + "inv_.emissionOn as emissionOn, \n"
            + "inv_.sri_clave_acceso as claveAcceso, \n"
            + "'001' as ptoEmi, \n"
            + "'001' as estab, \n"
            + "inv_.sequencial as secuencial,\n"
            + "invtotal.resumen as resumen, \n"
            + "invtotal.total as importeTotal \n"
            + "from public.invoice inv_ \n"
            + "inner join public.vista_subject_organization as emisor on emisor.subject_id = inv_.author\n"
            + "inner join public.subject as cliente on cliente.id = inv_.owner \n"
            + "inner join public.vista_invoice_total as invtotal on invtotal.invoice_id = inv_.id\n"
            + "where inv_.deleted=false "
            + "and inv_.author = :#{#authorId} \n"
            + "and inv_.organization_id = :#{#organizacionId} \n"
            + "and inv_.documenttype = :#{#documentType} \n"
            + "and not inv_.sri_clave_acceso is null order by inv_.emissionOn DESC")
    List<InvoiceView> listarPorAuthorYOrganizacionIdYDocumentType(@Param("authorId") Long authorId, @Param("organizacionId") Long organizacionId, @Param("documentType") int documentType);
    
    @Query(nativeQuery = true, value = "select inv_.id as id, inv_.uuid as uuid, \n"
            + "upper(cliente.firstname || ' ' || cliente.surname) as customerFullName, \n"
            + "emisor.organization_name as subjectFullName, \n"
            + "inv_.emissionOn as emissionOn, \n"
            + "inv_.sri_clave_acceso as claveAcceso, \n"
            + "'001' as ptoEmi, \n"
            + "'001' as estab, \n"
            + "inv_.sequencial as secuencial,\n"
            + "invtotal.resumen as resumen, \n"
            + "invtotal.total as importeTotal \n"
            + "from public.invoice inv_ \n"
            + "inner join public.vista_subject_organization as emisor on emisor.subject_id = inv_.author\n"
            + "inner join public.subject as cliente on cliente.id = inv_.owner \n"
            + "inner join public.vista_invoice_total as invtotal on invtotal.invoice_id = inv_.id\n"
            + "where inv_.deleted=false "
            + "and inv_.owner = :#{#ownerId} \n"
            + "and inv_.organization_id = :#{#organizacionId} \n"
            + "and inv_.documenttype = :#{#documentType} \n"
            + "and not inv_.sri_clave_acceso is null order by inv_.emissionOn DESC")
    List<InvoiceView> listarPorOwnerYOrganizacionIdYDocumentType(@Param("ownerId") Long ownerId, @Param("organizacionId") Long organizacionId, @Param("documentType") int documentType);

}
