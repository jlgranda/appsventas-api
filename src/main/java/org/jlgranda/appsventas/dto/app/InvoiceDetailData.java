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
package org.jlgranda.appsventas.dto.app;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author jlgranda
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class InvoiceDetailData {
    
    private BigDecimal amount;
    private ProductData product;
    
    public BigDecimal getSubTotal(){
        return this.getAmount().multiply(this.getProduct().getPrice());
    };

    public BigDecimal getImpuestoValor() {
        return this.getSubTotal().multiply(this.getProduct().getTaxFactor().divide(BigDecimal.valueOf(100)));
    }
    
    public boolean isIVA12(){
        
        if (this.getProduct() == null) return false;
        
        return "IVA".equalsIgnoreCase(this.getProduct().getTaxType().toString());
    }

    public Long getProductId() {
        return this.getProduct().getId();
    }

    public String getProductName() {
        return this.getProduct().getName();
    }

    public BigDecimal getPrice() {
        return this.getProduct().getPrice();
    }
}
