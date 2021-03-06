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

    public static final String JNDINAME_MASTER = "java:/FedeXADS";
    public static final String JNDINAME_STAGE = "java:/FedeXADS";
    public static final String DATA_DEFAULT_SCHEMA = "public";
    public static final String SRI_DEFAULT_SCHEMA = "public";

    public static final String AMBIENTE_PRODUCCION = "PRODUCCION";
    public static final String AMBIENTE_DESARROLLO = "DESARROLLO";

    public static final Long ESTADO_ACTIVO = 1L;
    public static final Long ESTADO_INACTIVO = 0L;
    public static final Long ESTADO_MANTENIMIENTO = 2L;
    public static final Long ESTADO_CREADA = 3L;
    public static final Long ESTADO_CERRADO = 4L;

    public static final String ESTADO_COMPROBANTE_CREADA = "CREADA";
    public static final String ESTADO_COMPROBANTE_RECIBIDA = "RECIBIDA";

    public static final String ACCION_COMPROBANTE_ANULAR = "anular";
    public static final String ACCION_COMPROBANTE_ENVIAR = "enviar";
    public static final String ACCION_COMPROBANTE_AUTORIZAR = "autorizar";
    public static final String ACCION_COMPROBANTE_EMITIR = "emitir"; //enviar+autorizar

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

    //Nombre de columnas fijas para las capas del sistema
    public static final String NOMBRE_COLUMNA_UUID = "uuid";
    public static final String NOMBRE_COLUMNA_USUARIO_CREACION = "reg_usu";
    public static final String NOMBRE_COLUMNA_USUARIO_MODIFICACION = "act_usu";
    public static final String NOMBRE_COLUMNA_FECHA_CREACION = "reg_fecha";
    public static final String NOMBRE_COLUMNA_FECHA_MODIFICACION = "act_fecha";
    public static final String NOMBRE_COLUMNA_SESSION_USERNAME = "session_user_name";

    public static final String PERFIL_BASE = "USUARIO";

    public static final String TIPO_INTERNO = "INTERNO";
    public static final String TIPO_EXTERNO = "EXTERNO";

    public static final String SERIE_PREFIJO = "FACTURA";
    public static final String ADMIN_USERNAME = "admin"; //El administrador
    public static final String PUBLIC_USERNAME = "jlgranda81@gmail.com"; //El administrador
    public static final Long ADMIN_ID = 1L; //El administrador

    public static final String CAMPO_SIN_UNIDAD = "Sin unidad";
    public static final String CAMPO_TEXTO = "TEXTO";

    public static final String REPORTES_CARPETA_DOCUMENTOS = "documentos";
    public static final String REPORTES_CARPETA_DEFINICIONES = "definiciones";
    public static final String REPORTES_CARPETA_SALIDAS = "salidas";

    public static final String MENSAJE = "Mensaje";
    public static final String NOTIFICACION = "Notificaci??n";

    public static final String FORMATO_FECHA = "yyyy-MM-dd";
    public static final String FORMATO_FECHA_SRI = "dd/MM/yyyy";
    public static final String FORMATO_FECHA_HORA = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
    public static final String FORMATO_FECHA_HORAZ = "yyyy-MM-dd'T'HH:mm:ss";

    public static final String FORMATO_FECHA_HORA_COMPLETO = "EEE MMM d yyyy HH:mm:ss";

    //Estados de aprobaci??n
    public static final String ESTADO_APROBADO = "APROBADO";
    public static final String ESTADO_NO_APROBADO = "NO APROBADO";

    public static final String HOY = "hoy";
    public static final String AHORA = "ahora";

    public static String CLARO = "CLARO";

    public static String MOTOR_PENTAHO = "pentaho";
    public static String MOTOR_JASPER = "jasper";

    public static final String VERONICA_NO_TOKEN = "NO TOKEN";

    public static final String INVOICE = "facturas";
    public static final String BOL = "guias-remision";
    public static final String CM = "notas-credito";
    public static final String DM = "notas-debito";
    public static final String WH = "retenciones";
    public static final String PC = "liquidaciones-compra";

    public static final String ICON_DEFAULT = "fa fa-question-circle";

    //Desde Veronica API
    public static final String URI_API_AUTH = "/oauth/token";
    public static final String URI_API_V1 = "/api/v1.0/";
    public static final String URI_API_V1_INVOICE = URI_API_V1 + "facturas";
    public static final String URI_API_V1_BOL = URI_API_V1 + "guias-remision";
    public static final String URI_API_V1_CM = URI_API_V1 + "notas-credito";
    public static final String URI_API_V1_DM = URI_API_V1 + "notas-debito";
    public static final String URI_API_V1_WH = URI_API_V1 + "retenciones";
    public static final String URI_API_V1_PC = URI_API_V1 + "liquidaciones-compra";
    public static final String URI_OPERATIONS = "/operaciones/";
    public static final String URI_PUBLIC = "/publico/";
    public static final String PASSWORD = "U;\"PsfDL?4F2X[@h";
    public static final String INVOICE_SEQUENCIAL = "001-001-00000";
    public static final String INVOICE_PAX = "1";
    public static final String INVOICE_BOARD = "0";
    public static final String INVOICE_UNIT = "u";

    public static final String NO_ORGANIZACION = "ORGANIZACION NO DEFINIDA";
    public static final String NO_RUC = "RUC NO VALIDO";
    public static final String CREADO_POR = "CREADOR POR";
    public static final String FAZIL = "FAZIL";
    public static final String PROFORMA = "PROFORMA";
    public static final String SIN_DIRECCION = "SIN DIRECCI??N";
    public static final String SIN_TELEFONO = "999999999";

    public static final String JSON_FACTURA_TEMPLATE_CONTRIBUYENTE_ESPECIAL = "{\n"
            + "   \"id\":\"$id\",\n"
            + "   \"version\":\"$version\",\n"
            + "   \"infoTributaria\": {\n"
            + "      \"agenteRetencion\": \"$agenteRetencion\",\n"
            + "      \"ambiente\": \"$ambiente\",\n"
            + "      \"claveAcceso\": \"$claveAcceso\",\n"
            + "      \"codDoc\": \"$codDoc\",\n"
            + "      \"contribuyenteRimpe\": \"$contribuyenteRimpe\",\n"
            + "      \"dirMatriz\": \"$dirMatriz\",\n"
            + "      \"estab\": \"$estab\",\n"
            + "      \"nombreComercial\": \"$nombreComercial\",\n"
            + "      \"ptoEmi\": \"$ptoEmi\",\n"
            + "      \"razonSocial\": \"$razonSocial\",\n"
            + "      \"regimenMicroempresas\": \"$regimenMicroempresas\",\n"
            + "      \"ruc\": \"$ruc\",\n"
            + "      \"secuencial\": \"$secuencial\",\n"
            + "      \"tipoEmision\": \"$tipoEmision\"\n"
            + "   },\n"
            + "   \"infoFactura\":{\n"
            + "      \"fechaEmision\":\"$fechaEmision\",\n"
            + "      \"dirEstablecimiento\":\"$dirEstablecimiento\",\n"
            + "      \"contribuyenteEspecial\":\"$contribuyenteEspecial\",\n"
            + "      \"obligadoContabilidad\":\"$obligadoContabilidad\",\n"
            + "      \"tipoIdentificacionComprador\":\"$tipoIdentificacionComprador\",\n"
            //+ "      \"guiaRemision\":\"$guiaRemision\",\n"
            + "      \"razonSocialComprador\":\"$razonSocialComprador\",\n"
            + "      \"identificacionComprador\":\"$identificacionComprador\",\n"
            + "      \"direccionComprador\":\"$direccionComprador\",\n"
            + "      \"totalSinImpuestos\":$totalSinImpuestos,\n"
            + "      \"totalDescuento\":$totalDescuento,\n"
            + "      \"totalImpuesto\":[\n"
            + "         {\n"
            + "            \"codigo\":\"$totalImpuestoCodigo\",\n"
            + "            \"codigoPorcentaje\":$totalImpuestoCodigoPorcentaje,\n"
            + "            \"descuentoAdicional\":$descuentoAdicional,\n"
            + "            \"baseImponible\":$totalImpuestoBaseImponible,\n"
            + "            \"valor\":$totalImpuestoValor\n"
            + "         }\n"
            + "      ],\n"
            + "      \"propina\":$propina,\n"
            + "      \"importeTotal\":$importeTotal,\n"
            + "      \"moneda\":\"$moneda\",\n"
            + "      \"pagos\":[\n"
            + "         {\n"
            + "            \"formaPago\":\"$formaPago\",\n"
            + "            \"total\":$total,\n"
            + "            \"plazo\":\"$plazo\",\n"
            + "            \"unidadTiempo\":\"$unidadTiempo\"\n"
            + "         }\n"
            + "      ],\n"
            + "      \"valorRetIva\":$valorRetIva,\n"
            + "      \"valorRetRenta\":$valorRetRenta\n"
            + "   },\n"
            + "   \"detalle\":[\n"
            + "     $detalles"
            + "   ],\n"
            + "   \"campoAdicional\":[]\n"
            + "}";

    public static final String JSON_FACTURA_TEMPLATE = "{\n"
            + "   \"id\":\"$id\",\n"
            + "   \"version\":\"$version\",\n"
            + "   \"infoTributaria\": {\n"
            + "      \"agenteRetencion\": \"$agenteRetencion\",\n"
            + "      \"ambiente\": \"$ambiente\",\n"
            + "      \"claveAcceso\": \"$claveAcceso\",\n"
            + "      \"codDoc\": \"$codDoc\",\n"
            + "      \"contribuyenteRimpe\": \"$contribuyenteRimpe\",\n"
            + "      \"dirMatriz\": \"$dirMatriz\",\n"
            + "      \"estab\": \"$estab\",\n"
            + "      \"nombreComercial\": \"$nombreComercial\",\n"
            + "      \"ptoEmi\": \"$ptoEmi\",\n"
            + "      \"razonSocial\": \"$razonSocial\",\n"
            + "      \"regimenMicroempresas\": \"$regimenMicroempresas\",\n"
            + "      \"ruc\": \"$ruc\",\n"
            + "      \"secuencial\": \"$secuencial\",\n"
            + "      \"tipoEmision\": \"$tipoEmision\"\n"
            + "   },\n"
            + "   \"infoFactura\":{\n"
            + "      \"fechaEmision\":\"$fechaEmision\",\n"
            + "      \"dirEstablecimiento\":\"$dirEstablecimiento\",\n"
            + "      \"obligadoContabilidad\":\"$obligadoContabilidad\",\n"
            + "      \"tipoIdentificacionComprador\":\"$tipoIdentificacionComprador\",\n"
            + "      \"razonSocialComprador\":\"$razonSocialComprador\",\n"
            + "      \"identificacionComprador\":\"$identificacionComprador\",\n"
            + "      \"direccionComprador\":\"$direccionComprador\",\n"
            + "      \"totalSinImpuestos\":$totalSinImpuestos,\n"
            + "      \"totalDescuento\":$totalDescuento,\n"
            + "      \"totalImpuesto\":[\n"
            + "         {\n"
            + "            \"codigo\":\"$totalImpuestoCodigo\",\n"
            + "            \"codigoPorcentaje\":$totalImpuestoCodigoPorcentaje,\n"
            + "            \"descuentoAdicional\":$descuentoAdicional,\n"
            + "            \"baseImponible\":$totalImpuestoBaseImponible,\n"
            + "            \"valor\":$totalImpuestoValor\n"
            + "         }\n"
            + "      ],\n"
            + "      \"propina\":$propina,\n"
            + "      \"importeTotal\":$importeTotal,\n"
            + "      \"moneda\":\"$moneda\",\n"
            + "      \"pagos\":[\n"
            + "         {\n"
            + "            \"formaPago\":\"$formaPago\",\n"
            + "            \"total\":$total,\n"
            + "            \"plazo\":\"$plazo\",\n"
            + "            \"unidadTiempo\":\"$unidadTiempo\"\n"
            + "         }\n"
            + "      ],\n"
            + "      \"valorRetIva\":$valorRetIva,\n"
            + "      \"valorRetRenta\":$valorRetRenta\n"
            + "   },\n"
            + "   \"detalle\":[\n"
            + "     $detalles"
            + "   ],\n"
            + "   \"campoAdicional\":[]\n"
            + "}";
    
    
    public static final String JSON_NOTA_CREDITO_TEMPLATE = "{\n"
            + "  \"campoAdicional\": [],\n"
            + "  \"detalle\": [\n"
            + "     $detalles"
            + "  ],\n"
            + "  \"infoNotaCredito\": {\n"
            + "    \"codDocModificado\": \"$codDocModificado\",\n"
            + "    \"compensacion\": [\n"
            + "     $compensaciones"
            + "    ],\n"
            + "    \"contribuyenteEspecial\": \"$contribuyenteEspecial\",\n"
            + "    \"dirEstablecimiento\": \"$dirEstablecimiento\",\n"
            + "    \"fechaEmision\": \"$fechaEmision\",\n"
            + "    \"fechaEmisionDocSustento\": \"$fechaEmisionDocSustento\",\n"
            + "    \"identificacionComprador\": \"$identificacionComprador\",\n"
            + "    \"moneda\": \"$moneda\",\n"
            + "    \"motivo\": \"$motivo\",\n"
            + "    \"numDocModificado\": \"$numDocModificado\",\n"
            + "    \"obligadoContabilidad\": \"$obligadoContabilidad\",\n"
            + "    \"razonSocialComprador\": \"$razonSocialComprador\",\n"
            + "    \"rise\": \"$rise\",\n"
            + "    \"tipoIdentificacionComprador\": \"$tipoIdentificacionComprador\",\n"
            + "    \"totalImpuesto\": [\n"
            + "      {\n"
            + "        \"baseImponible\": $baseImponible,\n"
            + "        \"codigo\": \"$codigo\",\n"
            + "        \"codigoPorcentaje\": \"$codigoPorcentaje\",\n"
            + "        \"descuentoAdicional\": $descuentoAdicional,\n"
            + "        \"tarifa\": $tarifa,\n"
            + "        \"valor\": $valor,\n"
            + "        \"valorDevolucionIva\": $valorDevolucionIva\n"
            + "      }\n"
            + "    ],\n"
            + "    \"totalSinImpuestos\": $totalSinImpuestos,\n"
            + "    \"valorModificacion\": $valorModificacion\n"
            + "  },\n"
            + "   \"id\":\"$id\",\n"
            + "   \"version\":\"$version\",\n"
            + "   \"infoTributaria\": {\n"
            + "      \"agenteRetencion\": \"$agenteRetencion\",\n"
            + "      \"ambiente\": \"$ambiente\",\n"
            + "      \"claveAcceso\": \"$claveAcceso\",\n"
            + "      \"codDoc\": \"$codDoc\",\n"
            + "      \"contribuyenteRimpe\": \"$contribuyenteRimpe\",\n"
            + "      \"dirMatriz\": \"$dirMatriz\",\n"
            + "      \"estab\": \"$estab\",\n"
            + "      \"nombreComercial\": \"$nombreComercial\",\n"
            + "      \"ptoEmi\": \"$ptoEmi\",\n"
            + "      \"razonSocial\": \"$razonSocial\",\n"
            + "      \"regimenMicroempresas\": \"$regimenMicroempresas\",\n"
            + "      \"ruc\": \"$ruc\",\n"
            + "      \"secuencial\": \"$secuencial\",\n"
            + "      \"tipoEmision\": \"$tipoEmision\"\n"
            + "   },\n"
            + "}";

    public static final String JSON_DETAIL_TEMPLATE = "{\n"
            + "         \"codigoPrincipal\":\"$codigoPrincipal\",\n"
            + "         \"codigoAuxiliar\":\"$codigoAuxiliar\",\n"
            + "         \"descripcion\":\"$descripcion\",\n"
            + "         \"cantidad\":$cantidad,\n"
            + "         \"precioUnitario\":$precioUnitario,\n"
            + "         \"descuento\":$descuento,\n"
            + "         \"precioTotalSinImpuesto\":$precioTotalSinImpuesto,\n"
            + "         \"detAdicional\":[\n"
            + "            {\n"
            + "               \"nombre\":\"$detAdicionalNombre1\",\n"
            + "               \"valor\":\"$detAdicionalValor1\"\n"
            + "            }\n"
            + "         ],\n"
            + "         \"impuesto\":[\n"
            + "            {\n"
            + "               \"codigo\":\"$impuestoCodigo\",\n"
            + "               \"codigoPorcentaje\":\"$impuestoCodigoPorcentaje\",\n"
            + "               \"tarifa\":$tarifa,\n"
            + "               \"baseImponible\":$impuestoBaseImponible,\n"
            + "               \"valor\":$impuestoValor\n"
            + "            }\n"
            + "         ]\n"
            + "      }\n";
    
    public static final String JSON_COMPENSACION_TEMPLATE = "{\n"
            + "        \"codigo\": \"$codigo\",\n"
            + "        \"tarifa\": $tarifa,\n"
            + "        \"valor\": $valor\n"
            + "      }\n";

    public static final String JSON_CERTIFICADO_DIGITAL = "{\n"
            + "  \"certificado\":\"$certificado\",\n"
            + "  \"password\": \"$password\"\n"
            + "}";

    public static final String SRI_STATUS_CREATED = "CREATED";
    public static final String SRI_STATUS_POSTED = "POSTED";
    public static final String SRI_STATUS_APPLIED = "APPLIED";
    public static final String SRI_STATUS_REJECTED = "REJECTED";
    public static final String SRI_STATUS_INVALID = "INVALID";
    public static final String SRI_STATUS_NO_APPLIED = "NO_APPLIED";
    public static final String SRI_STATUS_CANCELLED = "CANCELLED";
    public static final String SRI_ESTAB_DEFAULT = "001";
    public static final String SRI_PTO_EMISION_FACTURAS_ELECTRONICAS = "002";
}
