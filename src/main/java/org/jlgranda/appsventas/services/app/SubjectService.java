/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.Optional;
import org.jlgranda.appsventas.domain.Subject;
import org.jlgranda.appsventas.repository.app.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author usuario
 */
@Service
public class SubjectService {

    @Autowired
    private SubjectRepository repository;

    public SubjectRepository getRepository() {
        return repository;
    }

    /**
     * Devolver las instancias <tt>Subject</tt> para el id dado como parámentro,
     * discriminando el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<Subject> encontrarPorId(Long id) {
        return this.getRepository().encontrarPorId(id);
    }

    /**
     * Devolver las instancias <tt>Subject</tt> activos, discriminando el campo
     * eliminado
     *
     * @return
     */
    public List<Subject> encontrarActivos() {
        return this.getRepository().encontrarActivos();
    }

    /**
     * Devolver las instancias <tt>Subject</tt> para la palabra clave dada como
     * parámentro, discriminando el campo eliminado
     *
     * @param keyword
     * @return
     */
    public List<Subject> encontrarPorKeyword(String keyword) {
        return this.getRepository().encontrarPorKeyword(keyword);
    }

}
