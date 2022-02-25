/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.util;

/**
 *
 * @author usuario
 */
public enum TaxType {
    NONE,
    IVA,
    ICE,
    IRBPNR;
    
    public static TaxType encode(String code){
        TaxType t = NONE;
        if ("2".equalsIgnoreCase(code)){
            t = IVA;
        } else if ("3".equalsIgnoreCase(code)){
            t = ICE;
        } else if ("5".equalsIgnoreCase(code)){
            t = IRBPNR;
        } 
        return t;
    }
    
    public static String decode(TaxType t){
        String s = "";
        if (null != t)switch (t) {
            case NONE:
                s = "";
                break;
            case IVA:
                s = "2";
                break;
            case ICE:
                s = "3"; 
                break;
            case IRBPNR:
                s = "5";
                break;
            default:
                break;
        }
        return s;
    }
    
    public static String translate(TaxType t){
        String s = "";
        if (null != t)switch (t) {
            case NONE:
                s = "";
                break;
            case IVA:
                s = "IVA 12%";
                break;
            case ICE:
                s = "ICE"; 
                break;
            case IRBPNR:
                s = "IRBPNR";
                break;
            default:
                break;
        }
        return s;
    }
}
