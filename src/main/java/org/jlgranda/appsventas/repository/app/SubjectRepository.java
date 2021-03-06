/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @Query("select p from Subject p where p.deleted = false and p.id = :#{#id}")
    public Optional<Subject> encontrarPorId(Long id);

    @Query("select p from Subject p where p.deleted = false order by p.firstname ASC")
    public List<Subject> encontrarActivos();

    @Query("select p from Subject p, SubjectCustomer sc "
            + "where p.deleted = false and sc.deleted = false "
            + "and p.id = sc.customerId and sc.subjectId = :#{#subjectId} "
            + "order by p.firstname ASC")
    public List<Subject> encontrarPorSubjectCustomerSubjectId(Long subjectId);

    @Query("select p from Subject p where p.deleted = false "
            + "and (lower(trim(p.firstname)) like %:keyword% or lower(trim(p.surname)) like %:keyword% "
            + "or lower(trim(p.initials)) like %:keyword%  or lower(trim(p.code)) like %:keyword%) "
            + "order by p.firstname ASC")
    public List<Subject> encontrarPorKeyword(String keyword);

    @Query("select distinct p.initials from Subject p where p.deleted = false "
            + "and (p.initials != null and lower(trim(p.initials)) like %:keyword%) "
            + "order by p.initials ASC")
    public List<String> encontrarInitialsPorKeyword(String keyword);

}
