/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.util;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author usuario
 */
public class Statistics implements Serializable {

    private static final long serialVersionUID = -3341857100124128997L;

    private BigDecimal count;

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

}
