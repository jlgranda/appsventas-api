/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface SubjectCustomerRepository extends CrudRepository<SubjectCustomer, Long> {

    @Query("select p from SubjectCustomer p, Subject c "
            + "where p.deleted = false and c.deleted = false "
            + "and p.customerId = c.id "
            + "and p.subjectId = :#{#subjectId} "
            + "order by c.firstname ASC")
    public List<SubjectCustomer> encontrarPorSubjectId(Long subjectId);

    @Query("select p from SubjectCustomer p, Subject c "
            + "where p.deleted = false and c.deleted = false "
            + "and p.customerId = c.id "
            + "and (lower(trim(c.firstname)) like %:keyword% or lower(trim(c.surname)) like %:keyword% "
            + "or lower(trim(c.initials)) like %:keyword% or lower(trim(c.code)) like %:keyword%) "
            + "and p.subjectId = :#{#subjectId} "
            + "order by c.firstname ASC")
    public List<SubjectCustomer> encontrarPorSubjectIdYKeyword(Long subjectId, String keyword);

    @Query("select p from SubjectCustomer p, Subject c "
            + "where p.deleted = false and c.deleted = false "
            + "and p.customerId = c.id "
            + "and p.customerId = :#{#customerId} "
            + "order by c.firstname ASC")
    public List<SubjectCustomer> encontrarPorCustomerId(Long customerId);

    @Query("select p from SubjectCustomer p "
            + "where p.deleted = false "
            + "and p.subjectId = :#{#subjectId} "
            + "and p.customerId = :#{#customerId}")
    public Optional<SubjectCustomer> encontrarPorSubjectIdCustomerId(Long subjectId, Long customerId);

}
