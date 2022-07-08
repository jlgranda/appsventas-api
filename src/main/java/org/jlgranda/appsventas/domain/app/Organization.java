/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jlgranda.appsventas.domain.app;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.jlgranda.appsventas.domain.DeletableObject;

/**
 *
 * @author usuario
 */
@Entity
@Table(name = "Organization", schema = "public")
@Getter
@Setter
public class Organization extends DeletableObject<Organization> implements Serializable {

    private String ruc;
    private String initials;
    private String url;
    private String direccion;
    @Column(name = "registro_contable_habilitado")
    private Boolean accountingEnabled = Boolean.TRUE;
    @Column(name = "vista_ventas")
    private String vistaVentas;
    @Column(name = "vista_venta")
    private String vistaVenta;
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    @Column(name = "ambiente_sri")
    private String ambienteSRI;
    
    @Column(name = "contribuyente_especial")
    private Boolean contribuyenteEspecial = Boolean.FALSE;
    
    @Column(name = "contribuyente_especial_numero_resolucion")
    private String contribuyenteEspecialNumeroResolucion;
    
    @Column(name = "agente_retencion")
    private Boolean agenteRetencion = Boolean.FALSE;
    
    @Column(name = "regimen_rimpe")
    private Boolean regimenRimpe = Boolean.FALSE;
    @Column(name = "regimen_rimpe_tipo")
    private String regimenRimpeTipo;
    
    @Column(name = "regimen_microempresas")
    private Boolean regimenMicroEmpresas = Boolean.FALSE;
    
    @Column(name = "rise")
    private Boolean rise = Boolean.FALSE;
    
    @Column(name = "obligado_llevar_contabilidad")
    private Boolean obligadoLlevarContabilidad = Boolean.FALSE;
    
    @Column(name = "plan_id")
    private Long planId;

    public enum Type {
        GOVERMENT,
        PUBLIC,
        PRIVATE,
        NATURAL;

        private Type() {
        }
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Organization.Type organizationType;

    public String getCanonicalPath() {
        StringBuilder path = new StringBuilder();
        path.append(getName());
        return path.toString();
    }

    public List<Organization.Type> getOrganizationTypes() {
        return Arrays.asList(Organization.Type.values());
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + Objects.hashCode(this.ruc);
        hash = 83 * hash + Objects.hashCode(this.initials);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Organization other = (Organization) obj;
        if (!Objects.equals(this.ruc, other.ruc)) {
            return false;
        }
        if (!Objects.equals(this.initials, other.initials)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }
}
