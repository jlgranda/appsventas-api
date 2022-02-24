package org.jlgranda.appsventas.services;

import com.fasterxml.jackson.databind.JsonNode;
import org.jlgranda.appsventas.Constantes;
import org.jlgranda.appsventas.repository.DataRepository;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.tecnopro.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DataService {
        
    @Autowired
    private DataRepository dataRepository;
    
    public JsonNode getGeoJSON(String geoJSONQuery) {
        return dataRepository.executeGeoJSONQuery(geoJSONQuery);
    }

    @Transactional
    public void sqlddl(String sql) {  
        dataRepository.sqlddl(sql);
    }
    
    @Transactional
    public int ejecutar(String sql) {
        return dataRepository.ejecutarNativeQuery(sql);
    }
    
    public BigInteger ejecutarCount(String sql) {
        return dataRepository.ejecutarCountNativeQuery(sql);
    }
    
    public boolean isTableCreated(String tableSchema, String tableName) {
        String query = "SELECT count(*) FROM information_schema.tables\n"
                + "WHERE table_schema = '"
                + tableSchema
                + "' "
                + "and table_name = '"
                + tableName
                + "' \n"
                + "LIMIT 1;";
        // you will always get a single result
        BigInteger count = ejecutarCount(query);
        return (!(count.equals(BigInteger.ZERO)));
    }

    boolean verificarExistenciaTabla(StringBuilder tableName) {
        return dataRepository.checkForTable(tableName.toString());
    }
    
    /**
     * Encuentra la lista de valores relacionados definidos para una entidad <tt>Field</tt> de tipo <tt>RelationValue</tt>
     *
     * @param sql
     * @return una colección de objetos <tt>Option</tt>
     */
    public Map<String, Long> findUUIDColumnKeyMap(String sql) {
        List<String> columns = new ArrayList<>();
        Map<String, Long> map = new HashMap<>();
        String query = sql;
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(query, columns);
        Object[] rows = null;
        for (Object row : resultList) {
            rows = ((Object[]) row);
            map.put("" + rows[1], Long.valueOf("" + rows[0]));

        }
        return map;
    }
    
    /**
     * Encuentra la lista de valores relacionados definidos para una entidad <tt>Field</tt> de tipo <tt>RelationValue</tt>
     *
     * @param sql
     * @return una colección de objetos <tt>Option</tt>
     */
    public Map<Long, String> findResult(String sql) {
        List<String> columns = new ArrayList<>();
        Map<Long, String> map = new HashMap<>();
        String query = sql;
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(query, columns);
        Object[] rows = null;
        for (Object row : resultList) {
            rows = ((Object[]) row);
            map.put(Long.valueOf("" + rows[0]), "" + rows[1]);

        }
        return map;
    }
    
    /**
     * Encuentra la lista de valores númericos listados en la consulta SQL que retorna una sóla columna
     *
     * @param sql
     * @return una colección de objetos <tt>Option</tt>
     */
    public List<BigDecimal> findResultAsBigDecimalArray(String sql) {
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(sql, new ArrayList<>());
        List<BigDecimal> result = new ArrayList<>();
        Object[] rows = null;
        if (resultList != null){
            for (Object row : resultList) {
                rows = ((Object[]) row);
                for (Object row1 : rows) {
                    result.add(row1 != null ? BigDecimal.valueOf((double) row1) : BigDecimal.ZERO);
                }
            }
        }
        return result;
    }
    /**
     * Encuentra la lista de valores númericos listados en la consulta SQL que retorna una sóla columna
     *
     * @param sql
     * @return una colección de objetos <tt>Option</tt>
     */
    public List<BigDecimal> findResultAsBigDecimalArray2(String sql) {
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(sql, new ArrayList<>());
        List<BigDecimal> result = new ArrayList<>();
        //Object[] rows = null;
        if (resultList != null){
            for (Object row : resultList) {
                //rows = ((Object[]) row);
                //for (Object row1 : rows) {
                    result.add(row != null ? BigDecimal.valueOf((double) row) : BigDecimal.ZERO);
                //}
            }
        }
        return result;
    }
    
    /**
     * Encuentra la lista de valores númericos de tipo Double listados en la consulta SQL
     *
     * @param sql
     * @param columna número de columna de donde tomar el valor
     * @return una colección de objetos <tt>Option</tt>
     */
    public double[] findResultAsDoubleArray(String sql, int columna) {
        Map<String, BigDecimal> map = findResultAsMap(sql, columna);
        double[] result = new double[map.size()];
        int i = 0;
        for (BigDecimal bd: map.values()){
            result[i] = bd.doubleValue();
            //System.out.println(">>>>>>>>>>>>>>>>>>>>>< " + result[i]);
            i++;
        }
        
        return result;
    }
    /**
     * Encuentra mapa de valores (categoria, valor)
     *
     * @param sql número de columna de donde tomar el valor
     * @param columna
     * @return una colección de objetos <tt>Option</tt>
     */
    public Map<String, BigDecimal> findResultAsMap(String sql, int columna) {
        List<String> columns = new ArrayList<>();
        Map<String, BigDecimal> map = new HashMap<>();
        String query = sql;
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(query, columns);
        Object[] rows = null;
        for (Object row : resultList) {
            rows = ((Object[]) row);
            map.put("" + rows[0], (BigDecimal) rows[columna]);

        }
        return map;
    }
    public static String generarDML(String tipoDML, String tablename, String prefix, List<String> columns, List<String> values, String keyColumn, Object keyValue, String username) {

        //agregar columnas de auditoria
        Formatter fmt;
        StringBuilder dml = new StringBuilder(tipoDML);
        fmt = new Formatter(dml);
        if ("insert".equalsIgnoreCase(tipoDML)) {
            //Campos de auditoria
            columns.add(prefix + Constantes.NOMBRE_COLUMNA_USUARIO_CREACION);
            values.add(username);
            columns.add(prefix + Constantes.NOMBRE_COLUMNA_FECHA_CREACION);
            values.add("now()");
            columns.add(Constantes.NOMBRE_COLUMNA_SESSION_USERNAME);
            values.add(username);
            fmt.format(" INTO %s (%s) values (%s); ", tablename, Lists.toString(columns, ","), Lists.toString(values, ","));
        } else if ("update".equalsIgnoreCase(tipoDML)
                && keyValue != null) {

            //Campos de auditoria
            columns.add(prefix + Constantes.NOMBRE_COLUMNA_USUARIO_MODIFICACION);
            values.add(username);
            columns.add(prefix + Constantes.NOMBRE_COLUMNA_FECHA_MODIFICACION);
            values.add("now()");
            columns.add(Constantes.NOMBRE_COLUMNA_SESSION_USERNAME);
            values.add(username);

            List<String> sets = new ArrayList<>();
            for (int index = 0; index < columns.size(); index++) {
                sets.add(columns.get(index) + " = " + values.get(index));
            }
            if (keyValue instanceof Long) {
                fmt.format(" %s SET %s WHERE %s = %s; ", tablename, Lists.toString(sets, ","), keyColumn, keyValue);
            } else {
                fmt.format(" %s SET %s WHERE %s = '%s'; ", tablename, Lists.toString(sets, ","), keyColumn, keyValue);
            }

        } else if ("delete".equalsIgnoreCase(tipoDML)) {
            fmt.format(" FROM %s WHERE %s = %s; ", tablename, keyColumn, keyValue);
        }

        //System.out.println(">>>> generarDML: " + dml.toString());
        return dml.toString();
    }

    public List<String> findColumnasTabla(String nombreTabla) {
        String query = "SELECT c.table_name, c.column_name, c.is_nullable, c.data_type, c.character_maximum_length, c.numeric_precision, numeric_scale\n"
                + "FROM information_schema.columns c, pg_catalog.pg_tables t \n"
                + "where t.schemaname='sc_mapas'  AND t.tablename=c.table_name\n"
                + "and t.tablename='" + nombreTabla + "'";
        List<String> columns = new ArrayList<>(); //dummy
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(query, columns);
        Object[] rows = null;
        for (Object row : resultList) {
            rows = ((Object[]) row);
            columns.add("" + rows[1]);
        }
        return columns;
    }
    
    public List<String> findColumnasVista(String nombreVista) {
        String query = "SELECT c.table_name, c.column_name, c.is_nullable, c.data_type, c.character_maximum_length, c.numeric_precision, numeric_scale\n"
                + "FROM information_schema.columns c, pg_catalog.pg_views t \n"
                + "where t.schemaname='sc_mapas' AND t.viewname = c.table_name\n"
                + "and t.viewname='" + nombreVista + "'";
        List<String> columns = new ArrayList<>(); //dummy
        Collection<? extends Object> resultList = dataRepository.ejecutarNativeQuery(query, columns);
        Object[] rows = null;
        for (Object row : resultList) {
            rows = ((Object[]) row);
            columns.add("\"" +  rows[1] + "\"");
        }
        return columns;
    }
}
