/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.Invoice;
import org.jlgranda.appsventas.domain.util.DocumentType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {

    @Query("select p from Invoice p where p.deleted = false and p.organizacionId = :#{#organizacionId} order by p.emissionOn DESC")
    public List<Invoice> encontrarPorOrganizacionId(Long organizacionId);
    
@Query("select p from Invoice p where p.deleted = false and p.organizacionId = :#{#organizacionId} and p.documentType = :#{#documentType} order by p.emissionOn DESC")
    public List<Invoice> encontrarPorOrganizacionIdYDocumentType(Long organizacionId, DocumentType documentType);

}
