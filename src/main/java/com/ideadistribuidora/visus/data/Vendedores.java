package com.ideadistribuidora.visus.data;

import java.util.List;
import java.util.Set;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;

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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vendedores")
public class Vendedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idvendedor")
    private int idVendedor;

    @NotNull
    @Column(name = "iddocumento")
    private int idDocumento;

    @Column(name = "numero")
    private Long numeroDeDocumento;

    @NotNull
    @Column(name = "nombre")
    private String nombre;

    @NotNull
    @OneToOne
    @JoinColumn(name = "iddomicilio")
    private Domicilios idDomicilio;

    @Column(name = "telefono_fijo")
    private String telefonoFijo;

    @Column(name = "telefono_movil")
    private String telefonoMovil;

    @Column(name = "email")
    private String email;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnTransformer(read = "situacion_fiscal:: text", write = "?::sfiscal")
    private SituacionFiscalEnum situacionFiscal;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vendedores_zonas", joinColumns = @JoinColumn(name = "idvendedor"), inverseJoinColumns = @JoinColumn(name = "idzona"))
    private Set<Zonas> zonas;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vendedores_comisiones", joinColumns = @JoinColumn(name = "idvendedor"), inverseJoinColumns = @JoinColumn(name = "idcomision"))
    private Set<Comisiones> comisiones;
    @Transient
    private TipoDomicilioEnum tipoDomicilio;
    @Transient
    private String calle;
    @Transient
    private short numero;
    @Transient
    private String barrio;
    @Transient
    private String manzana;
    @Transient
    private String casa;
    @Transient
    private String sector;
    @Transient
    private String depto;
    @Transient
    private String oficina;
    @Transient
    private String lote;
    @Transient
    private Localidades localidades;

    // Getters and Setters
    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Long getNumeroDeDocumento() {
        return numeroDeDocumento;
    }

    public void setNumeroDeDocumento(Long numeroDeDocumento) {
        this.numeroDeDocumento = numeroDeDocumento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Domicilios getIdDomicilio() {
        return idDomicilio;
    }
    
    public void setIdDomicilio(Domicilios idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefonoFijo) {
        this.telefonoFijo = telefonoFijo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public SituacionFiscalEnum getSituacionFiscal() {
        return situacionFiscal;
    }

    public void setSituacionFiscal(SituacionFiscalEnum situacionFiscal) {
        this.situacionFiscal = situacionFiscal;
    }

    public Set<Zonas> getZonas() {
        return zonas;
    }

    public void setZonas(Set<Zonas> zonas) {
        this.zonas = zonas;
    }

    public Set<Comisiones> getComisiones() {
        return comisiones;
    }

    public void setComisiones(Set<Comisiones> comisiones) {
        this.comisiones = comisiones;
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

    public Localidades getLocalidades() {
        return localidades;
    }

    public void setLocalidades(Localidades localidades) {
        this.localidades = localidades;
    }
    
}
