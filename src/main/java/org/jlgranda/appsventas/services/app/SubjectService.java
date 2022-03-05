/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.services.app;

import java.util.List;
import java.util.Optional;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
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

    /**
     * Devolver el campo initials para la palabra clave dada como parámentro,
     * discriminando el campo eliminado
     *
     * @param keyword
     * @return
     */
    public List<String> encontrarInitialsPorKeyword(String keyword) {
        return this.getRepository().encontrarInitialsPorKeyword(keyword);
    }

    public Subject crearInstancia() {
        Subject _instance = new Subject();
        _instance.setCreatedOn(Dates.now());
        _instance.setLastUpdate(Dates.now());
        _instance.setStatus(StatusType.ACTIVE.toString());
        _instance.setActivationTime(Dates.now());
        _instance.setExpirationTime(Dates.addDays(Dates.now(), 364));
        return _instance;
    }

    public Subject crearInstancia(Subject user) {
        Subject _instance = crearInstancia();
        _instance.setAuthor(user); //Establecer al usuario actual
        _instance.setOwner(user); //Establecer al usuario actual
        return _instance;
    }

    public Subject guardar(Subject subject) {
        return this.getRepository().save(subject);
    }

}
