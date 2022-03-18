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
package org.jlgranda.appsventas.services.secuencias;

import java.util.Optional;
import java.util.UUID;
import net.tecnopro.util.Dates;
import org.jlgranda.appsventas.domain.StatusType;
import org.jlgranda.appsventas.domain.secuencias.Secuencia;
import org.jlgranda.appsventas.repository.secuencias.SecuenciaRepository;
import org.jlgranda.appsventas.util.secuencial.SecuencialInvoiceGenerator;
import org.jlgranda.fede.util.serial.Generator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author jlgranda
 */
@Service
public class SerialService {
    @Autowired
    private SecuenciaRepository repository;

    public SecuenciaRepository getRepository() {
        return repository;
    }

    /**
     * Devolver la instancia <tt>Secuencia</tt> para el id dado como parámetro sin
     * discriminar el campo eliminado
     *
     * @param id
     * @return
     */
    public Optional<Secuencia> findById(Long id) {
        return this.getRepository().findById(id);
    }

    public Secuencia crearInstancia() {
        Secuencia instancia = new Secuencia();
        instancia.setStatus(StatusType.ACTIVE.toString());
        instancia.setUuid(UUID.randomUUID().toString());
        instancia.setCreatedOn(Dates.now());
        instancia.setLastUpdate(Dates.now());
        return instancia;
    }

    public Secuencia guardar(Secuencia secuencia) {
        return this.getRepository().save(secuencia);
    }

    private Optional<Secuencia> encontrarPorEntidadEstabPtoEmi(String ruc, String entidad, String estab, String ptoEmi) {
        Optional<Secuencia> secuenciaOpt =  this.getRepository().encontrarPorEntidadEstabPtoEmi(entidad, estab, ptoEmi);
        if (!secuenciaOpt.isPresent()){
            //Crear el secuencial
            Secuencia secuencia = this.crearInstancia();
            secuencia.setRuc(ruc);
            secuencia.setEntidad(entidad);
            secuencia.setName(entidad);
            secuencia.setEstab(estab);
            secuencia.setPtoEmi(ptoEmi);
            secuencia.setDigitos(9L);
            secuencia.setValorActual(1L);
            secuencia.setAgregarAnio(false);
            this.guardar(secuencia);
            secuenciaOpt = Optional.of(secuencia);
        } else {
            
                //Cargar el secuencial y almacenar el paso
                Secuencia secuencia = secuenciaOpt.get();
                //Incrementa 1 y registra el valor usado
                Long valorActual = secuencia.getValorActual() + 1;
                secuencia.setValorActual(valorActual); //Una lectura del generador incremente el valor actual
                //Guardar los nuevos valores de la serie
                this.guardar(secuencia);
                secuenciaOpt = Optional.of(secuencia);
        }
        return secuenciaOpt;
    }
    
    public Generator getSecuencialGenerator(String ruc, String entidad, String estab, String ptoEmi) {
        Optional<Secuencia> secuenciaOpt = this.encontrarPorEntidadEstabPtoEmi(ruc, entidad, estab, ptoEmi);
        SecuencialInvoiceGenerator secuencialInvoiceGenerator = new SecuencialInvoiceGenerator();
        BeanUtils.copyProperties(secuenciaOpt.get(), secuencialInvoiceGenerator);
        secuencialInvoiceGenerator.setAddYear(secuenciaOpt.get().isAgregarAnio());
        secuencialInvoiceGenerator.setSeed(secuenciaOpt.get().getValorActual());
        secuencialInvoiceGenerator.setDigits( secuenciaOpt.get().getDigitos().intValue() );
        return secuencialInvoiceGenerator;
    }
    
}
