/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.app.SubjectCustomer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface SubjectCustomerRepository extends CrudRepository<SubjectCustomer, Long> {

    @Query("select p from SubjectCustomer p, Subject s "
            + "where p.deleted = false and p.subjectId = s.id and p.subjectId = :#{#subjectId} "
            + "order by s.firstname ASC")
    public List<SubjectCustomer> encontrarPorSubjectId(Long subjectId);

    @Query("select p from SubjectCustomer p, Subject s "
            + "where p.deleted = false and p.subjectId = s.id and s.deleted = false and p.subjectId = :#{#subjectId} "
            + "and (lower(s.firstname) like %:keyword% or lower(s.surname) like %:keyword% or lower(s.initials) like %:keyword%  or lower(s.code) like %:keyword%) "
            + "order by s.firstname ASC")
    public List<SubjectCustomer> encontrarPorSubjectIdYKeyword(Long subjectId, String keyword);

}
