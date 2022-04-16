/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.dto.app;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.util.ProductType;
import org.jlgranda.appsventas.domain.util.TaxType;

/**
 *
 * @author usuario
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = false)
public class ProductData {

    private Long id;
    private String uuid;

    private String name;
    private BigDecimal price;
    private ProductType productType;
    private TaxType taxType;
    private BigDecimal taxFactor;
    private String icon;
    private byte[] photo;

    //Auxiliares
    private String categoryName;

}
