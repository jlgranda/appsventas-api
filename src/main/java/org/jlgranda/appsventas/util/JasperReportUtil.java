/*
 * Licensed under the TECNOPRO License, Version 1.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.tecnopro.net/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jlgranda.appsventas.util;

import org.jlgranda.appsventas.Constantes;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.OutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import org.springframework.jndi.JndiObjectFactoryBean;


/**
 * The Class ReportUtil.
 *
 * @author jlgranda
 */
public class JasperReportUtil  implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 8814862516698789539L;

    private static JasperReportUtil instance;
    
    private JasperReportUtil(){}
    
    public static synchronized JasperReportUtil getInstance(){
        if(instance == null){
            instance = new JasperReportUtil();
        }
        return instance;
    }

    /**
     * Generar reporte.
     *
     * @param rutaDirectorioSalida
     * @param rutaArchivoJasper
     * @param params the params
     * @return the string
     * @throws JRException the JR exception
     */
    public String generarReporte(String rutaDirectorioSalida, String rutaArchivoJasper, Map<String, Object> params) throws JRException {
        String nombreReporteSalida = "";
        
        Connection conn = null;
        OutputStream output = null;
        try {
            InitialContext initialContext = null;
            DataSource ds = null;

            initialContext = new InitialContext();
            
            JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
            jndiObjectFactoryBean.setJndiName(Constantes.JNDINAME_MASTER);
            try {
                jndiObjectFactoryBean.afterPropertiesSet();
            } catch (NamingException e) {
                Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, "Error while retrieving datasource with JNDI name jdbc/jndidatasource {0}", e);
            }
            
            ds = (DataSource) jndiObjectFactoryBean.getObject();
            conn = ds.getConnection();

            JasperReport jasperReport = null;

            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(rutaArchivoJasper);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, params, conn);

            //nombreReporteSalida = Constantes.REPORTE_BASE_NAME + System.currentTimeMillis() + ".pdf";
            nombreReporteSalida = "";

            String nombreDocumento = rutaDirectorioSalida + nombreReporteSalida;
            output = new FileOutputStream(
                    new File(nombreDocumento));
            JasperExportManager.exportReportToPdfStream(jasperPrint, output);

        } catch (NamingException | SQLException | JRException | IOException e) {
            Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace(System.out);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }
        }

        return nombreReporteSalida;
    }
    /**
     * Generar reporte.
     *
     * @param rutaDirectorioSalida
     * @param rutaArchivoJasper
     * @param params the params
     * @return the string
     * @throws JRException the JR exception
     */
    public String generarReporteExcel(String rutaDirectorioSalida, String rutaArchivoJasper, Map<String, Object> params) throws JRException {
        String nombreReporteSalida = "";
        
        Connection conn = null;
        OutputStream output = null;
        try {
            InitialContext initialContext = null;
            DataSource ds = null;

            initialContext = new InitialContext();
            
            JndiObjectFactoryBean jndiObjectFactoryBean = new JndiObjectFactoryBean();
            jndiObjectFactoryBean.setJndiName(Constantes.JNDINAME_MASTER);
            try {
                jndiObjectFactoryBean.afterPropertiesSet();
            } catch (NamingException e) {
                Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, "Error while retrieving datasource with JNDI name jdbc/jndidatasource {0}", e);
            }
            
            ds = (DataSource) jndiObjectFactoryBean.getObject();
            conn = ds.getConnection();

            JasperReport jasperReport = null;

            params.put(JRParameter.IS_IGNORE_PAGINATION, true);
            
            jasperReport = (JasperReport) JRLoader.loadObjectFromFile(rutaArchivoJasper);
            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    jasperReport, params, conn);

            //nombreReporteSalida = Constantes.REPORTE_BASE_NAME + System.currentTimeMillis() + ".pdf";
            nombreReporteSalida = "";

            String nombreDocumento = rutaDirectorioSalida + nombreReporteSalida;
            output = new FileOutputStream(
                    new File(nombreDocumento));
            //JasperExportManager.exportReportToPdfStream(jasperPrint, output);
            SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
            configuration.setOnePagePerSheet(true);
            configuration.setDetectCellType(true); // Detect cell types (date and etc.)
            configuration.setWhitePageBackground(false); // No white background!
            configuration.setFontSizeFixEnabled(false);

            // No spaces between rows and columns 
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setRemoveEmptySpaceBetweenColumns(true);

            // If you want to name sheets then uncomment this line
            // configuration.setSheetNames(new String[] { "Data" });
            final JRXlsExporter exporter = new JRXlsExporter();
            exporter.setConfiguration(configuration);
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));

            ByteArrayOutputStream excelStream = new ByteArrayOutputStream();
            OutputStreamExporterOutput exporterOutput = new SimpleOutputStreamExporterOutput(excelStream);
            exporter.setExporterOutput(exporterOutput);
            exporter.exportReport();

            excelStream.writeTo(output);

        } catch (NamingException | SQLException | JRException | IOException e) {
            Logger.getLogger(JasperReportUtil.class.getName()).log(Level.SEVERE, e.getLocalizedMessage());
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    ex.printStackTrace(System.out);
                }
            }
            if (output != null) {
                try {
                    output.close();
                } catch (IOException ex) {
                    ex.printStackTrace(System.out);
                }
            }
        }

        return nombreReporteSalida;
    }
    

}

