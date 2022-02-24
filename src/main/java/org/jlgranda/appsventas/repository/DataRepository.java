package org.jlgranda.appsventas.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Collection;
import java.util.List;
import net.tecnopro.util.Strings;
import org.hibernate.Session;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class DataRepository {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    /**
     * Ejecuta sentencia SQL INSERT o UPDATE, no SELECT
     * @param sql la sentencia SQL INSERT o UPDATE, no SELECT
     * @return el número de entidades actualizadas
     */
    public int ejecutarNativeQuery(String sql){
        Query nativeQuery = em.createNativeQuery(sql);
        em.joinTransaction();
        return nativeQuery.executeUpdate();
    }
    
    /**
     * Ejecuta sentencia SQL INSERT o UPDATE, no SELECT
     * @param sql la sentencia SQL INSERT o UPDATE, no SELECT
     * @return el número de entidades actualizadas
     */
    public BigInteger ejecutarCountNativeQuery(String sql){
        if (Strings.isNullOrEmpty(sql)){
            return BigInteger.ZERO;
        } else if (!sql.toLowerCase().contains("count(")){
            return BigInteger.ZERO;
        }
        //Es una consulta con count
        Query nativeQuery = em.createNativeQuery(sql);
        return (BigInteger) nativeQuery.getSingleResult();
    }
    
    public Collection<? extends Object> ejecutarNativeQuery(String query, List<String> columnas) {
        Query nativeQuery = em.createNativeQuery(query);
        return nativeQuery.getResultList();
        
    }
    
    /**
     * Ejecuta consulta GeoJSON indicada
     * Ref.: https://vladmihalcea.com/how-to-map-json-collections-using-jpa-and-hibernate/
     * @param jsonObjectQuery
     * @return ojeto GeoJSON directo para cargar en capa de mapa.
     */
    public JsonNode executeGeoJSONQuery(String jsonObjectQuery) {
        JsonNode GeoJSONObject = null;
        if (!Strings.isNullOrEmpty(jsonObjectQuery)){
            GeoJSONObject = (JsonNode) em.createNativeQuery(jsonObjectQuery)
                .unwrap(org.hibernate.query.NativeQuery.class)
                .addScalar("jsonObject", JsonNodeBinaryType.INSTANCE)
                    .getSingleResult();
        }
        return GeoJSONObject;
    }

    public boolean checkForTable(final String tableName){
        
        //em.getTransaction().begin();
        ResultSet rs = null;
        boolean exists = false;
        Session session = em.unwrap(Session.class);
        Integer count = session.doReturningWork(new ReturningWork<Integer>() {
 
            @Override
            public Integer execute(Connection con) throws SQLException {
                // do something useful
                try (PreparedStatement stmt = con.prepareStatement("SELECT count(*) FROM " + tableName)) {
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    return rs.getInt(1);
                } catch (SQLException e) {
                    return -1;
                }
            }

        });

        return count.compareTo(0) >= 0;
    }
    
    public void sqlddl(String sql) {
        jdbcTemplate.execute(sql);
    }
}
