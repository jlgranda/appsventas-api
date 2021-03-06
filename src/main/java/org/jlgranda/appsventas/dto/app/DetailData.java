/*
 * Copyright (C) 2022 usuario
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
package org.jlgranda.appsventas.dto.app;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class DetailData {

    private Long id;
    private String uuid;

    private Long invoiceId;
    private Long productId;
    private String productName;
    private BigDecimal amount;
    private String unit;
    private BigDecimal price;
    private boolean iva12;
    
    public BigDecimal getSubTotal(){
        return this.getAmount().multiply(this.getPrice());
    };

    public BigDecimal getImpuestoValor() {
        BigDecimal factor = this.iva12 ? BigDecimal.valueOf(0.12) : BigDecimal.ZERO;
        return this.getSubTotal().multiply(factor);
    }

}
