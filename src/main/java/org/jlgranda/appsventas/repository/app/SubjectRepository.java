/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.repository.app;

import java.util.List;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author usuario
 */
public interface SubjectRepository extends CrudRepository<Subject, Long> {

    @Query("select p from Subject p where p.deleted = false")
    public List<Subject> encontrarActivos();

    @Query("select p from Subject p where p.deleted = false and (lower(p.firstname) like %:keyword% or lower(p.surname) like %:keyword% or lower(p.initials) like %:keyword%  or lower(p.code) like %:keyword%)")
    public List<Subject> encontrarPorKeyword(String keyword);

}
