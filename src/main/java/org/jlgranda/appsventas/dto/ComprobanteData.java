/*
 * Copyright (C) 2022 jlgranda
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jlgranda.appsventas.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 *<comprobante>
        <claveAcceso>1702201205176001321000110010030001000011234567816</claveAcceso>
        <mensajes>
            <mensaje>
            <identificador>35</identificador>
            <mensaje>DOCUMENTO INVÁLIDO</mensaje>
            <informacionAdicional>Se encontró el siguiente error en la estructura del comprobante: cvc-
            complex-type.2.4.a: Invalid content was found starting with element 'totalSinImpuestos'. One
            of '{fechaEmisionDocSustento}' is expected.</informacionAdicional>
            <tipo>ERROR</tipo>
            </mensaje>
        </mensajes>
    </comprobante>
 * @author jlgranda
 */
@Data
public class ComprobanteData {
    private String claveAcceso;
    private List<MensajeData> mensajes = new ArrayList<>();
}
