package com.ideadistribuidora.visus.data;

import java.util.List;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "domicilios")
public class Domicilios {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddomicilio")
    private int idDomicilio;
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "tipodomicilio")
    @ColumnTransformer(read = "tipodomicilio:: text", write = "?::tdomicilio")
    private TipoDomicilioEnum tipoDomicilio;
    @NotNull
    @Column(name = "calle")
    private String calle;
    @NotNull
    @Column(name = "numero")
    private short numero;
    @Column(name = "barrio")
    private String barrio;
    @Column(name = "manzana")
    private String manzana;
    @Column(name = "casa")
    private String casa;
    @Column(name = "sector")
    private String sector;
    @Column(name = "depto")
    private String depto;
    @Column(name = "oficina")
    private String oficina;
    @Column(name = "lote")
    private String lote;
    @ManyToOne
    @JoinColumn(name = "idlocalidad", nullable = false)
    private Localidades localidad;

    @ManyToMany(mappedBy = "domicilios")
    private List<Clientes> clientes;

    @Transient
    private String direccion;

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(int idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public TipoDomicilioEnum getTipoDomicilio() {
        return tipoDomicilio;
    }

    public void setTipoDomicilio(TipoDomicilioEnum tipoDomicilio) {
        this.tipoDomicilio = tipoDomicilio;
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public short getNumero() {
        return numero;
    }

    public void setNumero(short numero) {
        this.numero = numero;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getManzana() {
        return manzana;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getCasa() {
        return casa;
    }

    public void setCasa(String casa) {
        this.casa = casa;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getOficina() {
        return oficina;
    }

    public void setOficina(String oficina) {
        this.oficina = oficina;
    }

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }

    public Localidades getLocalidad() {
        return localidad;
    }

    public void setLocalidad(Localidades localidad) {
        this.localidad = localidad;
    }

    public List<Clientes> getClientes() {
        return clientes;
    }

    public void setClientes(List<Clientes> clientes) {
        this.clientes = clientes;
    }

}
