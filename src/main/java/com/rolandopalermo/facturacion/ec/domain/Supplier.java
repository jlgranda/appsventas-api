package com.rolandopalermo.facturacion.ec.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jlgranda.appsventas.domain.DeletableObject;

@Getter
@Setter
@ToString
@Entity
@Table(name = "proveedor")
public class Supplier extends DeletableObject<Supplier> implements Serializable {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "supplier_id", updatable = false, nullable = false)
//    private long id;
    
    @Column(name = "credito_maximo_monto")
    private Long creditoMaximoMonto;

    @Column(name = "credito_maximo_dias")
    private Long creditoMaximoDias;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_preferida_pago")
    private Date fechaPreferidaPago;
    
    @Column(name = "organization_id", insertable = true, updatable = true, nullable = true)
    private Long organizacionId;
//
//    @Column
//    private String businessName;
//
//    @Column
//    private byte[] logo;
//
//    @Column
//    private boolean isDeleted;

    public String getIdNumber() {
        return getOwner().getCode();
    }

    public void setIdNumber(String idNumber) {
        if ( this.getOwner() != null ){
            this.getOwner().setCode(idNumber);
        }
    }

    public String getBusinessName() {
        if ( this.getOwner() != null ){
            return this.getOwner().getFirstname() + " " + this.getOwner().getSurname() + ", " + this.getOwner().getInitials();
        }
        
        return "";
    }

    public void setBusinessName(String businessName) {
        if ( this.getOwner() != null ){
            this.getOwner().setName(businessName);
        }
    }

    public byte[] getLogo() {
        if ( this.getOwner() != null ){
            return this.getOwner().getPhoto();
        }
        return new byte[0];
    }

    public void setLogo(byte[] logo) {
        //this.logo = logo;
    }

    
    public void setIsDeleted(boolean isDeleted) {
        //this.isDeleted = isDeleted;
    }

    public Long getOrganizacionId() {
        return organizacionId;
    }

    public void setOrganizacionId(Long organizacionId) {
        this.organizacionId = organizacionId;
    }

    
    @Override
    public String toString() {
        return String.format("%s[id=%d]", getClass().getSimpleName(), getId());
    }
}