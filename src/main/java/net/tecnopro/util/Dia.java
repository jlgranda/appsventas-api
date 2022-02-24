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
 * Clase Auxiliar para manejo de los D'ias.
 *
 * @author Flor Mar'ia Vargas <a href="mailto:flor.vargas@tecnopro.net">Flor Mar'ia Vargas</a>
 * @version $Revision: 1.00 $ [1/09/2012]
 */

public class Dia {


	/**
	 * Retorna el Nombre del dia en base al numero del dia.
	 *
	 * @param numeroDia the numero dia
	 * @return the mes nombre largo
	 */
	public String getMesNombreLargo(int numeroDia) {
		String nombreDia = "";
		if (numeroDia == 1) {
			nombreDia = "Domingo";
		}
		if (numeroDia == 2) {
			nombreDia = "Lunes";
		}
		if (numeroDia == 3) {
			nombreDia = "Martes";
		}
		if (numeroDia == 4) {
			nombreDia = "Mi\u00E9rcoles";
		}
		if (numeroDia == 5) {
			nombreDia = "Jueves";
		}
		if (numeroDia == 6) {
			nombreDia = "Viernes";
		}
		if (numeroDia == 7) {
			nombreDia = "S\u00E1bado";
		}
		return nombreDia;
	}
	
}
