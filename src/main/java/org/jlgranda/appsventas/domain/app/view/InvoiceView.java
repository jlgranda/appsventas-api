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
package org.jlgranda.appsventas.domain.app.view;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Estructura para lectura de datos Invoice
 *
 * @author jlgranda
 */
public interface InvoiceView {

    public Long getId();

    public String getUuid();

    public Date getEmissionOn();

    public String getResumen();
    
    public BigDecimal getAmount();

    public BigDecimal getSubTotal();

    public BigDecimal getDescuento();

    public BigDecimal getIva0Total();

    public BigDecimal getIva12Total();

    public Boolean getIva12();

    public BigDecimal getPropina();

    public BigDecimal getImporteTotal();

    public Long getCustomerId();

    public String getCustomerFullName();

    public String getSubjectFullName();

    public String getEstab();

    public String getPtoEmi();

    public String getSecuencial();

    public String getClaveAcceso();

    public String getInternalStatus();

    public Boolean getIsPayment();

    public String getCustomerRUC();

    public String getCustomerEmail();

    public String getNumeroAutorizacion();

    public Date getAuthorizationDate();
    
}
