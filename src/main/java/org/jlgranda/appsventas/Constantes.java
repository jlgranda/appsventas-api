package org.jlgranda.appsventas;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author jlgranda
 */
public class Constantes {

    

    public static final String JNDINAME_MASTER = "java:/SMCMasterDS";
    public static final String JNDINAME_STAGE = "java:/SMCStageDS";
    public static final String DATA_DEFAULT_SCHEMA = "public";
    public static final String MAPAS_DEFAULT_SCHEMA = "sc_facturacion";

    public static final Long ESTADO_ACTIVO = 1L;
    public static final Long ESTADO_INACTIVO = 0L;
    public static final Long ESTADO_MANTENIMIENTO = 2L;
    public static final Long ESTADO_CREADA = 3L;
    public static final Long ESTADO_CERRADO = 4L;

    public static final String ETAPA_INICIO = "INICIO";
    public static final String ETAPA_EJECUCION = "EJECUCION";
    public static final String ETAPA_FINAL = "FINAL";

    public static final String ESTADO_ELIMINADO = "_ELIMINADO_";
    public static final String REPORTE_BASE_NAME = "appsventas_report_";
    public static final Long DUMMY_PERSON_ID = -999L;
    public static final Long MIL = 1000L;
    public static final String SEPARADOR = "_";
    public static final String SEPARADOR_CATEGORIAS = "/";
    public static final String SEPARADOR_NOMBRES = " ";
    public static final String VACIO = "";
    public static final String ESPACIO = " ";

    public static final String IMPORTAR_COLUMNA_GRADO = "grado";
    public static final String IMPORTAR_COLUMNA_NOMBRE = "nombre";

    public static final String FID = "fid";
    public static final String ID = "id";
    public static final String LABEL = "label";
    public static final String CODIGO = "codigo";
    public static final String NOMBRE = "nombre";
    public static final String DESCRIPCION = "descripcion";
    public static final String GEOM = "geom";
    public static final String GEOMETRY = "geometry";
    public static final String PATH = "path";
    public static final String CO_ID = "co_id"; //Columna llave para filtros de acceso a la información
    public static final String TT_ID = "tratct_id"; //Columna llave para filtros de acceso a la información
    public static final String EVN_ID = "evn_id"; //Columna llave para filtros de acceso a la información
    public static final String RSG = "camp_riesgo"; //Columna llave para filtros de acceso a la información
    public static final String EVNTP_ID = "evntp_id"; //Columna llave para filtros de acceso a la información
    public static final String OP_ID = "op_id"; //Columna llave para filtros de acceso a la información
    public static final String CTR_ID = "catrdt_id"; //Columna llave para filtros de acceso a la información
    public static final String FE_REG = "fecha_registro"; //Columna llave para filtros de acceso a la información
    public static final String PR_ID = "proceso_id"; //Columna llave para filtros de acceso a la información
    public static final String EST = "camp_estado"; //Columna llave para filtros de acceso a la información
    public static final String PROV = "provincia_codigo"; //Columna llave para filtros de acceso a la información
    public static final String CANT = "canton_codigo"; //Columna llave para filtros de acceso a la información
    public static final String PARR = "parroquia_nombre"; //Columna llave para filtros de acceso a la información
    public static final String APB = "aprobado"; //Columna llave para filtros de acceso a la información
    public static final String COD = "unq_codigo"; //Columna llave para filtros de acceso a la información

    //Nombre de columnas fijas para las capas del sistema
    public static final String NOMBRE_COLUMNA_UUID = "uuid";
    public static final String NOMBRE_COLUMNA_USUARIO_CREACION = "reg_usu";
    public static final String NOMBRE_COLUMNA_USUARIO_MODIFICACION = "act_usu";
    public static final String NOMBRE_COLUMNA_FECHA_CREACION = "reg_fecha";
    public static final String NOMBRE_COLUMNA_FECHA_MODIFICACION = "act_fecha";
    public static final String NOMBRE_COLUMNA_SESSION_USERNAME = "session_user_name";

    public static final String PERFIL_BASE = "USUARIO";

    //Clases de Equipos
    public static final String EQUIPO_LOGISTICO = "mediologistico";
    public static final String EQUIPO_COMUNICACION = "equipodecomunicacion";
    public static final String EQUIPO_RASTREO = "equipoderastreo";
    
     //Tipos de Unidades Operacionales
    public static final String COMANDO_OPERATIVO_TIPO_UNIDAD_OPERATIVA = "UNIDAD OPERATIVA";
    public static final String COMANDO_OPERATIVO_TIPO_GRUPO_OPERATIVO = "GRUPO OPERATIVO";
    public static final String COMANDO_OPERATIVO_TIPO_COMANDO_OPERATIVO = "COMANDO OPERATIVO";
    
    public static final String TIPO_INTERNO = "INTERNO";
    public static final String TIPO_EXTERNO = "EXTERNO";
    
    //Módulos
    public static final String MODULO_OPERACIONES_ENTIDAD = "operacion";
    public static final String MODULO_EVENTOS_ENTIDAD = "evento";
    public static final String MODULO_RECINTOELECTORALEVENTOS_ENTIDAD = "recinto_electoral_evento";
    public static final String MODULO_PARTESEQUIPOMEDIO_ENTIDAD = "parte_equipo_medio";
    public static final String MODULO_PARTESMEDIOLOGISTICO_ENTIDAD = "parte_medio_logistico";
    public static final String MODULO_PARTESMEDIOCOMUNICACION_ENTIDAD = "parte_medio_comunicacion";
    public static final String MODULO_PARTESPERSONAL_ENTIDAD = "parte_personal";
    public static final String MODULO_PROCESOSELECTORALES_ENTIDAD = "proceso_electoral";
    public static final String SERIE_PREFIJO = "CC.FF.AA";
    public static final Long ADMIN_ID = 1L; //El administrador
    
    public static final String CAMPO_SIN_UNIDAD = "Sin unidad";
    public static final String CAMPO_TEXTO = "TEXTO";
    
    public static final String REPORTES_CARPETA_DOCUMENTOS = "documentos";
    public static final String REPORTES_CARPETA_DEFINICIONES = "definiciones";
    public static final String REPORTES_CARPETA_SALIDAS = "salidas";
    
    //Matriz de evaluacion
    public static final String MATRIZ_MISION = "mision";
    public static final String MATRIZ_ACCION = "accion";
    public static final String MATRIZ_OPERACION = "operacionPredefinida";
    public static final String MATRIZ_TAREA = "tareaTactica";
    
    public static final String MENSAJE = "Mensaje";
    public static final String NOTIFICACION = "Notificación";
    public static final String TAREA_TACTICA_PREFIJO = "TTAC";

    public static final String FORMATO_FECHA = "yyyy-MM-dd";
    public static final String FORMATO_FECHA_HORA = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String FORMATO_FECHA_HORAZ = "yyyy-MM-dd'T'HH:mm:ss";
    

    public static final String FORMATO_FECHA_HORA_COMPLETO = "EEE MMM d yyyy HH:mm:ss";

    //Estados de aprobación
    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_NO_APROBADO = "NO APROBADO";
    
    
    public static final String HOY = "hoy";
    public static final String AHORA = "ahora";
    
    
    public static final String CAPA_RASTER = "raster";
    public static final String CAPA_GPX = "GPX";
    
    
    public static String MAPLAYER_TYPE_NO_GEOMETRY = "No_geometry";
    public static String MAPLAYER_TYPE_WMS_WMTS = "WMS/WMTS";
    public static String MAPLAYER_TYPE_XYZ = "XYZ";
    public static String MAPLAYER_QGIS_TYPE_RASTER = "raster";
    public static String MAPLAYER_QGIS_TYPE_DATASET = "dataset";
    public static String MAPLAYER_QGIS_GPX = "dataset";
    
    public static String MAPLAYER_TYPE_POINT = "Point";
    public static String MAPLAYER_TYPE_LINE = "Line";
    public static String MAPLAYER_TYPE_MULTILINE = "MultiLine";
    public static String MAPLAYER_TYPE_POLYGON = "Polygon";
    public static String MAPLAYER_TYPE_MULTIPOLYGON = "MultiPolygon";
    
    public static Long ZOOM_LEVEL = 149L;
    public static Long ZOOM_LEVEL_TOWN = 38187L;
    public static Long ZOOM_LEVEL_CITY = 76373L;
    public static Long ZOOM_LEVEL_METROPOLITAN = 152746L;

    public static Long TAREA_REPORTES_ID = 38166L;
    public static Boolean VERIFICACION_USUARIO_PARTES = Boolean.FALSE;
    public static Boolean VERIFICACION_UNIDAD_PARTES = Boolean.FALSE;
    
    public static String CLARO = "CLARO";
    public static String SMC = "SMC";
    
    public static String MOTOR_PENTAHO = "pentaho";
    public static String MOTOR_JASPER = "jasper";
    
}
