/*
 * Copyright 2012  SIGCATASTROS TECNOPRO CIA. LTDA.
 *
 * Licensed under the TECNOPRO License, Version 1.0 (the "License");
 * you may not use this file or reproduce complete or any part without express autorization from TECNOPRO CIA LTDA.
 * You may obtain a copy of the License at
 *
 *     http://www.tecnopro.net
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.tecnopro.util;

// TODO: Auto-generated Javadoc
/**
 * Clase Auxiliar para manejo de los Meses.
 *
 * @author Flor Mar'ia Vargas <a href="mailto:flor.vargas@tecnopro.net">Flor
 * Mar'ia Vargas</a>
 * @version $Revision: 1.00 $ [1/09/2012]
 */
public class Mes {

    /**
     * Instantiates a new mes.
     */
    public Mes() {
        super();
    }

    /**
     * Muestra el Nombre de un Mes en base al numero del mismo.
     *
     * @param valorNumero the valor numero
     * @return the mes correspondiente
     */
    public Mes getMesCorrespondiente(int valorNumero) {
        Mes m = new Mes();
        if (valorNumero == 1) {
            m = getMes01();
        }
        if (valorNumero == 2) {
            m = getMes02();
        }
        if (valorNumero == 3) {
            m = getMes03();
        }
        if (valorNumero == 4) {
            m = getMes04();
        }
        if (valorNumero == 5) {
            m = getMes05();
        }
        if (valorNumero == 6) {
            m = getMes06();
        }
        if (valorNumero == 7) {
            m = getMes07();
        }
        if (valorNumero == 8) {
            m = getMes08();
        }
        if (valorNumero == 9) {
            m = getMes09();
        }
        if (valorNumero == 10) {
            m = getMes10();
        }
        if (valorNumero == 11) {
            m = getMes11();
        }
        if (valorNumero == 12) {
            m = getMes12();
        }
        return m;
    }

    /**
     * Setea los datos del objeto Mes.
     *
     * @param valorNumero the valor numero
     * @param nombreCorto the nombre corto
     * @param nombreLargo the nombre largo
     * @param nombreInformal the nombre informal
     */
    public Mes(int valorNumero, String nombreCorto, String nombreLargo, String nombreInformal) {
        super();
        this.valorNumero = valorNumero;
        this.nombreCorto = nombreCorto;
        this.nombreLargo = nombreLargo;
        this.nombreInformal = nombreInformal;
    }

    /**
     * The valor numero.
     */
    private Integer valorNumero;

    /**
     * The nombre corto.
     */
    private String nombreCorto;

    /**
     * The nombre largo.
     */
    private String nombreLargo;

    /**
     * The nombre informal.
     */
    private String nombreInformal;

    /**
     * The mes01.
     */
    private Mes mes01;

    /**
     * The mes02.
     */
    private Mes mes02;

    /**
     * The mes03.
     */
    private Mes mes03;

    /**
     * The mes04.
     */
    private Mes mes04; //;

    /**
     * The mes05.
     */
    private Mes mes05; //;

    /**
     * The mes06.
     */
    private Mes mes06; //;

    /**
     * The mes07.
     */
    private Mes mes07; //

    /**
     * The mes08.
     */
    private Mes mes08; //

    /**
     * The mes09.
     */
    private Mes mes09; //

    /**
     * The mes10.
     */
    private Mes mes10; //

    /**
     * The mes11.
     */
    private Mes mes11; //

    /**
     * The mes12.
     */
    private Mes mes12; //

    /**
     * Gets the mes01.
     *
     * @return the mes01
     */
    public Mes getMes01() {
        mes01 = new Mes(1, "ENE", "ENERO", "Enero");
        return mes01;
    }

    /**
     * Sets the mes01.
     *
     * @param mes01 the new mes01
     */
    public void setMes01(Mes mes01) {
        this.mes01 = mes01;
    }

    /**
     * Gets the mes02.
     *
     * @return the mes02
     */
    public Mes getMes02() {
        mes02 = new Mes(2, "FEB", "FEBRERO", "Febrero");
        return mes02;
    }

    /**
     * Sets the mes02.
     *
     * @param mes02 the new mes02
     */
    public void setMes02(Mes mes02) {
        this.mes02 = mes02;
    }

    /**
     * Gets the mes03.
     *
     * @return the mes03
     */
    public Mes getMes03() {
        mes03 = new Mes(3, "MAR", "MARZO", "Marzo");
        return mes03;
    }

    /**
     * Sets the mes03.
     *
     * @param mes03 the new mes03
     */
    public void setMes03(Mes mes03) {
        this.mes03 = mes03;
    }

    /**
     * Gets the mes04.
     *
     * @return the mes04
     */
    public Mes getMes04() {
        mes04 = new Mes(4, "ABR", "ABRIL", "Abril");
        return mes04;
    }

    /**
     * Sets the mes04.
     *
     * @param mes04 the new mes04
     */
    public void setMes04(Mes mes04) {
        this.mes04 = mes04;
    }

    /**
     * Gets the mes05.
     *
     * @return the mes05
     */
    public Mes getMes05() {
        mes05 = new Mes(5, "MAY", "MAYO", "Mayo");
        return mes05;
    }

    /**
     * Sets the mes05.
     *
     * @param mes05 the new mes05
     */
    public void setMes05(Mes mes05) {
        this.mes05 = mes05;
    }

    /**
     * Gets the mes06.
     *
     * @return the mes06
     */
    public Mes getMes06() {
        mes06 = new Mes(6, "JUN", "JUNIO", "Junio");
        return mes06;
    }

    /**
     * Sets the mes06.
     *
     * @param mes06 the new mes06
     */
    public void setMes06(Mes mes06) {
        this.mes06 = mes06;
    }

    /**
     * Gets the mes07.
     *
     * @return the mes07
     */
    public Mes getMes07() {
        mes07 = new Mes(7, "JUL", "JULIO", "Julio");
        return mes07;
    }

    /**
     * Sets the mes07.
     *
     * @param mes07 the new mes07
     */
    public void setMes07(Mes mes07) {
        this.mes07 = mes07;
    }

    /**
     * Gets the mes08.
     *
     * @return the mes08
     */
    public Mes getMes08() {
        mes08 = new Mes(8, "AGO", "AGOSTO", "Agosto");
        return mes08;
    }

    /**
     * Sets the mes08.
     *
     * @param mes08 the new mes08
     */
    public void setMes08(Mes mes08) {
        this.mes08 = mes08;
    }

    /**
     * Gets the mes09.
     *
     * @return the mes09
     */
    public Mes getMes09() {
        mes09 = new Mes(9, "SEP", "SEPTIEMBRE", "Septiembre");
        return mes09;
    }

    /**
     * Sets the mes09.
     *
     * @param mes09 the new mes09
     */
    public void setMes09(Mes mes09) {
        this.mes09 = mes09;
    }

    /**
     * Gets the mes10.
     *
     * @return the mes10
     */
    public Mes getMes10() {
        mes10 = new Mes(10, "OCT", "OCTUBRE", "Octubre");
        return mes10;
    }

    /**
     * Sets the mes10.
     *
     * @param mes10 the new mes10
     */
    public void setMes10(Mes mes10) {
        this.mes10 = mes10;
    }

    /**
     * Gets the mes11.
     *
     * @return the mes11
     */
    public Mes getMes11() {
        mes11 = new Mes(11, "NOV", "NOVIEMBRE", "Noviembre");
        return mes11;
    }

    /**
     * Sets the mes11.
     *
     * @param mes11 the new mes11
     */
    public void setMes11(Mes mes11) {
        this.mes11 = mes11;
    }

    /**
     * Gets the mes12.
     *
     * @return the mes12
     */
    public Mes getMes12() {
        mes12 = new Mes(12, "DEC", "DICIEMBRE", "Diciembre");
        return mes12;
    }

    /**
     * Sets the mes12.
     *
     * @param mes12 the new mes12
     */
    public void setMes12(Mes mes12) {
        this.mes12 = mes12;
    }

    /**
     * Gets the valor numero.
     *
     * @return the valor numero
     */
    public int getValorNumero() {
        return valorNumero;
    }

    /**
     * Sets the valor numero.
     *
     * @param valorNumero the new valor numero
     */
    public void setValorNumero(int valorNumero) {
        this.valorNumero = valorNumero;
    }

    /**
     * Gets the nombre corto.
     *
     * @return the nombre corto
     */
    public String getNombreCorto() {
        return nombreCorto;
    }

    /**
     * Sets the nombre corto.
     *
     * @param nombreCorto the new nombre corto
     */
    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    /**
     * Gets the nombre largo.
     *
     * @return the nombre largo
     */
    public String getNombreLargo() {
        return nombreLargo;
    }

    /**
     * Sets the nombre largo.
     *
     * @param nombreLargo the new nombre largo
     */
    public void setNombreLargo(String nombreLargo) {
        this.nombreLargo = nombreLargo;
    }

    /**
     * Retorna el objeto mes en base al n'umero del Mes.
     *
     * @param nroMes the nro mes
     * @return the mes
     */
    public Mes retornarMes_Numero(int nroMes) { // NOPMD
        Mes mes = new Mes();
        if (nroMes == 1) {
            mes = new Mes(1, "ENE", "ENERO", "Enero");
        }
        if (nroMes == 2) {
            mes = new Mes(2, "FEB", "FEBRERO", "Febrero");
        }
        if (nroMes == 3) {
            mes = new Mes(3, "MAR", "MARZO", "Marzo");
        }
        if (nroMes == 4) {
            mes = new Mes(4, "ABR", "ABRIL", "Abril");
        }
        if (nroMes == 5) {
            mes = new Mes(5, "MAY", "MAYO", "Mayo");
        }
        if (nroMes == 6) {
            mes = new Mes(6, "JUN", "JUNIO", "Junio");
        }
        if (nroMes == 7) {
            mes = new Mes(7, "JUL", "JULIO", "Julio");
        }
        if (nroMes == 8) {
            mes = new Mes(8, "AGO", "AGOSTO", "Agosto");
        }
        if (nroMes == 9) {
            mes = new Mes(9, "SEP", "SEPTIEMBRE", "Septiembre");
        }
        if (nroMes == 10) {
            mes = new Mes(10, "OCT", "OCTUBRE", "Octubre");
        }
        if (nroMes == 11) {
            mes = new Mes(11, "NOV", "NOVIEMBRE", "Noviembre");
        }
        if (nroMes == 12) {
            mes = new Mes(12, "DEC", "DICIEMBRE", "Diciembre");
        }
        return mes;
    }

    /* (non-Javadoc)
   	 * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        int hash = 0;
        hash += valorNumero != null ? valorNumero.hashCode() : 0;
        return hash;
    }

    /* (non-Javadoc)
    	 * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Mes)) {
            return false;
        }
        Mes other = (Mes) object;
        return !(this.valorNumero == null && other.valorNumero != null || this.valorNumero != null && !this.valorNumero.equals(other.valorNumero));
    }

    /* (non-Javadoc)
    	 * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "net.tecnopro.tecnopro.Mes[ valorNumero=" + valorNumero + " ]";
    }

    /**
     * Gets the nombre informal.
     *
     * @return the nombre informal
     */
    public String getNombreInformal() {
        return nombreInformal;
    }

    /**
     * Sets the nombre informal.
     *
     * @param nombreInformal the new nombre informal
     */
    public void setNombreInformal(String nombreInformal) {
        this.nombreInformal = nombreInformal;
    }

}
