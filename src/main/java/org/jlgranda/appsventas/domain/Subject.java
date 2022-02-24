/*
 * Copyright (C) 2015 jlgranda
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jlgranda.appsventas.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.persistence.*;
import net.tecnopro.util.Strings;


/**
 * Un sujeto que tiene acceso y usa el sistema. 
 * Puede ser una persona, organización y/o sistema
 * @author jlgranda
 */

@Entity
@Table(name = "Subject")
/*
 * Consultas nombradas para Subject
 */
@NamedQueries({
    @NamedQuery(name = "Subject.findUserByLogin", query = "select u from Subject u where u.username = ?1"),
    @NamedQuery(name = "Subject.findByCode", query = "select s from Subject s where s.code = ?1"),
    @NamedQuery(name = "Subject.findUsersByNameOrUsername", query = "select u from Subject u where lower(u.username)  LIKE lower(:name) or lower(u.name) LIKE lower(:name)"),
    @NamedQuery(name = "Subject.findUserByEmail", query = "from Subject u where u.email = ?1"),
    @NamedQuery(name = "Subject.findUserByUUID", query = "from Subject u where u.uuid = ?1")})

public class Subject extends DeletableObject<Subject> implements Serializable {

    private static final long serialVersionUID = 274770881776410973L;
    @Column(nullable = true)
    private String firstname;
    @Column(nullable = true)
    private String surname;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = true)
    private String passwordSalt;
    @Column(length = 1024)
    @Basic(fetch = FetchType.LAZY)
    private byte[] photo;
    @Column(nullable = false, length = 128, unique = true)
    private String email;
    private String mobileNumber;
    private String workPhoneNumber;
    @Column
    private boolean confirmed;
    @Transient
    private boolean loggedIn;
    @Transient

    @Column
    private boolean emailSecret = true;
    @Column(length = 50)
    private String screenName;
    @Column
    private String bio;
    
    @Column(nullable = true, length = 128, unique = true)
    private String fedeEmail;
    @Column(nullable = true, length = 128)
    private String fedeEmailPassword;
    
    @Column(nullable = true, unique = true)
    private String ruc;
    private String initials;
    private String numeroContribuyenteEspecial;
    
    //Se establece a objeto Boolean por compatibilidad con los datos existentes
    @Column(nullable = true)
    protected Boolean contactable;
    
    @Column(nullable = true)
    protected Boolean nonnative;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private Date date_birth;

    /**
     * Return for each BussinesEntity te canonical path ....
     * @return 
     */
    @Transient
    public String getCanonicalPath(){
        return getName();
    }
    
    public enum Type {
        NATURAL,
        GOVERMENT,
        PUBLIC,
        PRIVATE,
        SYSTEM;
        private Type() {
        }
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Subject.Type subjectType;

    public Type getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Type subjectType) {
        this.subjectType = subjectType;
    }
    
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username != null) {
            username = username.toLowerCase();
        }
        this.username = username;
    }

    public void setPassword(String password) {
        //this.password = new BasicPasswordEncryptor().encryptPassword(password);
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public String getWorkPhoneNumber() {
        return workPhoneNumber;
    }

    public void setWorkPhoneNumber(String workPhoneNumber) {
        this.workPhoneNumber = workPhoneNumber;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(final String screenName) {
        this.screenName = screenName;
    }

    public boolean isEmailSecret() {
        return emailSecret;
    }

    public void setEmailSecret(final boolean emailSecret) {
        this.emailSecret = emailSecret;
    }

    public boolean isUsernameConfirmed() {
        return confirmed;
    }

    public void setUsernameConfirmed(final boolean confirmed) {
        this.confirmed = confirmed;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public String getFedeEmail() {
        return fedeEmail;
    }

    public void setFedeEmail(String fedeEmail) {
        this.fedeEmail = fedeEmail;
    }

    public String getFedeEmailPassword() {
        return fedeEmailPassword;
    }

    public void setFedeEmailPassword(String fedeEmailPassword) {
        this.fedeEmailPassword = fedeEmailPassword;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getNumeroContribuyenteEspecial() {
        return numeroContribuyenteEspecial;
    }

    public void setNumeroContribuyenteEspecial(String numeroContribuyenteEspecial) {
        this.numeroContribuyenteEspecial = numeroContribuyenteEspecial;
    }

    public void setContactable(Boolean contactable) {
        this.contactable = contactable;
    }
    
    public Boolean getContactable() {
        return this.contactable;
    }

    public void setNonnative(Boolean nonnative) {
        this.nonnative = nonnative;
    }

    public Boolean getNonnative() {
        return this.nonnative;
    }

    public Date getDate_birth() {
        return date_birth;
    }

    public void setDate_birth(Date date_birth) {
        this.date_birth = date_birth;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = (prime * result) + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Subject other = (Subject) obj;
        if (username == null) {
            if (other.username != null) {
                return false;
            }
        } else if (!username.equals(other.username)) {
            return false;
        }
        return true;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    
    @Transient
    public String getFullName() {
        StringBuilder builder = new StringBuilder();
        if (!Strings.isNullOrEmpty(this.getFirstname()))
            builder.append(this.getFirstname());
        
        if (!Strings.isNullOrEmpty(this.getSurname())){
            builder.append(" ");
            builder.append(this.getSurname());
        }
        
        if (!Strings.isNullOrEmpty(this.getEmail()) && builder.length() == 0){
            builder.append(this.getEmail());
        }
        
        String fullName = builder.toString();
        boolean flag = Strings.isNullOrEmpty(fullName);
        return flag ? getUsername() : fullName;
    }
    
    @Transient
    public String getEmailAddress(){
        return getFullName() + " <" + getEmail() + ">";
    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
    
    public List<Subject.Type> getSubjectTypes() {
        return Arrays.asList(Subject.Type.values());
    }
}
