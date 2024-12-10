package com.ideadistribuidora.visus.data;

import java.util.Set;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

@Entity
public class Proveedores {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idproveedor")
    private int idProveedor;
    @Column(name = "iddocumento")
    private int idDocumento;
    @Column(name = "numero")
    private Long numero;
    @Column(name = "nombre_fantasia")
    private String nombreFantasia;
    @Column(name = "nombre_real")
    private String nombreReal;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "proveedores_domicilios", joinColumns = @JoinColumn(name = "idproveedor"), inverseJoinColumns = @JoinColumn(name = "iddomicilio"))
    private Set<Domicilios> domicilios;
    @Column(name = "telefono_uno")
    private String telefono1;
    @Column(name = "telefono_dos")
    private String telefono2;
    @Column(name = "telefono_tres")
    private String telefono3;
    // Relación muchos a muchos con Banco, usando la entidad intermedia ClienteBanco
    @OneToMany(mappedBy = "proveedores", cascade = CascadeType.ALL)
    private Set<ProveedoresBancos> proveedoresBancos;

    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "situacion_fiscal:: text", write = "?::sfiscal")
    private SituacionFiscalEnum situacionFiscal;
    @Email
    @Column(name = "email")
    private String email;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
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

    public SituacionFiscalEnum getSituacionFiscal() {
        return situacionFiscal;
    }

    public void setSituacionFiscal(SituacionFiscalEnum situacionFiscal) {
        this.situacionFiscal = situacionFiscal;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Domicilios> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(Set<Domicilios> domicilios) {
        this.domicilios = domicilios;
    }

    public Set<ProveedoresBancos> getProveedoresBancos() {
        return proveedoresBancos;
    }

    public void setProveedoresBancos(Set<ProveedoresBancos> proveedoresBancos) {
        this.proveedoresBancos = proveedoresBancos;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

}
