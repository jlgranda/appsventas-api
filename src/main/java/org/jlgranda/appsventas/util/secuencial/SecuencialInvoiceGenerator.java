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
package org.jlgranda.appsventas.util.secuencial;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import net.tecnopro.util.Lists;
import net.tecnopro.util.Strings;
import org.jlgranda.fede.util.serial.Generator;

/**
 *
 * @author jlgranda
 */
public final class SecuencialInvoiceGenerator implements Generator, Serializable {

    private static final long serialVersionUID = 8609846404414924601L;
    
    private boolean addYear;
    private int digits;
    private String estab;
    private String ptoEmi;

    private Long seed = 0L;

    public SecuencialInvoiceGenerator() {
        init();
    }
    
    public SecuencialInvoiceGenerator(String estab, String ptoEmi, boolean addYear, int digits, Long seed) {
        setEstab(estab);
        setPtoEmi(ptoEmi);
        setAddYear(addYear);
        setDigits(digits);
        setSeed(seed);
    }

    @Override
    public boolean isAddYear() {
        return addYear;
    }

    public void setAddYear(boolean addYear) {
        this.addYear = addYear;
    }

    @Override
    public int getDigits() {
        return digits;
    }

    public void setDigits(int digits) {
        this.digits = digits;
    }

    @Override
    public String getEstab() {
        return estab;
    }

    public void setEstab(String estab) {
        this.estab = estab;
    }

    @Override
    public String getPtoEmi() {
        return ptoEmi;
    }

    public void setPtoEmi(String ptoEmi) {
        this.ptoEmi = ptoEmi;
    }

    
    @Override
    public String getYear() {
        if (!isAddYear())
			return "-";

		Calendar c = Calendar.getInstance();
		return "-" + String.valueOf(c.get(Calendar.YEAR)) + "-";
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    private void init() {
        setEstab("001");
        setPtoEmi("001");
        setAddYear(false);
        setDigits(8);
        setSeed(Calendar.getInstance().getTimeInMillis());
    }

    @Override
    public String next() {
        
        //this.setSeed(this.getSeed() + 1L); //Se incrementa al cargar
        List<String> partes = new ArrayList<>();
        partes.add( this.getEstab() );
        partes.add( this.getPtoEmi() );
        if (this.isAddYear()){
            partes.add( this.getYear() );
        }
        partes.add( Strings.complete(this.getDigits(), this.getSeed(), '0')) ;
        return Lists.toString(partes, "-");
    }
    
    public static void main (String [] args){
        SecuencialInvoiceGenerator ag = new SecuencialInvoiceGenerator();
        SecuencialInvoiceGenerator ag1 = new SecuencialInvoiceGenerator("001", "001", false, 9, 0L);
        System.err.println(">> " + ag.next());
        System.err.println(">> " + ag.next());
        System.err.println(">> " + ag.next());
        System.err.println(">> " + ag.next());
        System.err.println(">> " + ag.next());
        System.err.println(">> " + ag1.next());
        System.err.println(">> " + ag1.next());
        System.err.println(">> " + ag1.next());
        System.err.println(">> " + ag1.next());
        System.err.println(">> " + ag1.next());
    }
}
