package com.ideadistribuidora.visus.data;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;

@Entity
public class Proveedores extends AbstractEntity {

    private String idDocumento;
    private String numero;
    private String nombreFantasia;
    private String nombreReal;
    private String idDomicilio;
    private String telefono1;
    private String telefono2;
    private String telefono3;
    private String telefonoFax;
    private String idLocalidad;
    private String idBanco;
    private String situacionFiscal;
    @Email
    private String email;

    public String getIdDocumento() {
        return idDocumento;
    }
    public void setIdDocumento(String idDocumento) {
        this.idDocumento = idDocumento;
    }
    public String getNumero() {
        return numero;
    }
    public void setNumero(String numero) {
        this.numero = numero;
    }
    public String getNombreFantasia() {
        return nombreFantasia;
    }
    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
    }
    public String getNombreReal() {
        return nombreReal;
    }
    public void setNombreReal(String nombreReal) {
        this.nombreReal = nombreReal;
    }
    public String getIdDomicilio() {
        return idDomicilio;
    }
    public void setIdDomicilio(String idDomicilio) {
        this.idDomicilio = idDomicilio;
    }
    public String getTelefono1() {
        return telefono1;
    }
    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }
    public String getTelefono2() {
        return telefono2;
    }
    public void setTelefono2(String telefono2) {
        this.telefono2 = telefono2;
    }
    public String getTelefono3() {
        return telefono3;
    }
    public void setTelefono3(String telefono3) {
        this.telefono3 = telefono3;
    }
    public String getTelefonoFax() {
        return telefonoFax;
    }
    public void setTelefonoFax(String telefonoFax) {
        this.telefonoFax = telefonoFax;
    }
    public String getIdLocalidad() {
        return idLocalidad;
    }
    public void setIdLocalidad(String idLocalidad) {
        this.idLocalidad = idLocalidad;
    }
    public String getIdBanco() {
        return idBanco;
    }
    public void setIdBanco(String idBanco) {
        this.idBanco = idBanco;
    }
    public String getSituacionFiscal() {
        return situacionFiscal;
    }
    public void setSituacionFiscal(String situacionFiscal) {
        this.situacionFiscal = situacionFiscal;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

}
