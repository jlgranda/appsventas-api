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
package org.jlgranda.appsventas.dto;

import org.jlgranda.appsventas.Constantes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.tecnopro.util.Lists;
import net.tecnopro.util.Strings;

/**
 *
 * @author jlgranda
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class SerialData {

    private String entidad;
    private String prefijo;
    private String comandoOperacional;
    private String referencia;
    private String serie;
    private Long siguienteValor;

    private Long seed = 0L;

    private boolean agregarAnio;
    private boolean agregarMes;
    private boolean agregarDia;
    
    private boolean reiniciarPorAnio;
    private boolean reiniciarPorMes;
    private boolean reiniciarPorDia;
    
    /**
     * Almacena el valor de grupo actual relacionado con la columna reiniciar por
     */
    String reiniciarValorActual;

    private Long digitos;

    public SerialData() {
        setSeed(Calendar.getInstance().getTimeInMillis());
    }

    public String getAnio() {
        if (!isAgregarAnio()) {
            return Constantes.VACIO;
        }

        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
    }

    public String getMes() {
        if (!isAgregarMes()) {
            return Constantes.VACIO;
        }

        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.MONTH) + 1); //Para ajustar que ENERO es 0
    }

    public String getDia() {
        if (!isAgregarDia()) {
            return Constantes.VACIO;
        }

        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.DAY_OF_MONTH));
    }

    public String buildSerie() {
        
        String reiniciarValor = "";
        List<String> partes = new ArrayList<>();
        //partes.add(this.getPrefijo());
        if (this.getPrefijo().equalsIgnoreCase(this.getComandoOperacional())) {
            partes.add(this.getPrefijo());
        } else {
            partes.add(this.getComandoOperacional());
        }
        if (!Strings.isNullOrEmpty(this.getReferencia())) {
            partes.add(this.getReferencia());
        }
        if (this.isAgregarAnio()) {
            partes.add(this.getAnio());
        }
        if (this.isAgregarMes()) {
            partes.add(this.getMes());
        }
        if (this.isAgregarDia()) {
            partes.add(this.getDia());
        }
        
        partes.add(Strings.complete(this.getDigitos().intValue(), this.getSiguienteValor(), '0'));
                
        return Lists.toString(partes, "_");
    }
    public String buildSerieMilitar() {
        
        String reiniciarValor = "";
        List<String> partes = new ArrayList<>();
        //partes.add(this.getPrefijo());
        if (this.getPrefijo().equalsIgnoreCase(this.getComandoOperacional())) {
            partes.add(this.getPrefijo());
        } else {
            partes.add(this.getComandoOperacional());
        }
        if (!Strings.isNullOrEmpty(this.getReferencia())) {
            partes.add(this.getReferencia());
        }
        
        String marcaTiempo = "";
        
        if (this.isAgregarDia()) {
            marcaTiempo = marcaTiempo + this.getDia();
        }
        if (this.isAgregarMes()) {
            marcaTiempo = marcaTiempo + Strings.toString(this.getMes(), "mes");
        }
        if (this.isAgregarAnio()) {
            marcaTiempo = marcaTiempo + this.getAnio();
        }
        
        if (!Strings.isNullOrEmpty(marcaTiempo)){
            partes.add(marcaTiempo);
        }
        
        partes.add(Strings.complete(this.getDigitos().intValue(), this.getSiguienteValor(), '0'));
                
        return Lists.toString(partes, "_");
    }
}
