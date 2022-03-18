package org.jlgranda.appsventas.repository;

import java.util.List;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import org.jlgranda.appsventas.domain.Subject;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

@Repository
public interface UserRepository extends CrudRepository<Subject, Long> {

    @Query("select p from Subject p where p.deleted = false and p.username = :#{#username}")
    Optional<Subject> findByUsername(@Param("username") String username);

    Optional<Subject> findByEmail(String email);

    Optional<Subject> findByCode(String code);

    Optional<Subject> findByRuc(String ruc);

    @Query("select p from Subject p where p.deleted = false and p.uuid = :#{#uuid}")
    Optional<Subject> findByUUID(@Param("uuid") String uuid);

    /**
     * Obtener lista de entidades activas, ordenadas por fecha de modificación.
     * La úlitma modificada primero.
     *
     * @return lista de entidades
     */
    @Query("select p from Subject p where p.deleted = false order by p.lastUpdate desc")
    List<Subject> encontrarActivos();

}
