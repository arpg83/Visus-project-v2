package com.ideadistribuidora.visus.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

import org.hibernate.annotations.ColumnTransformer;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.enums.TipoTransporteEnum;

@Entity
@Table(name = "transportistas")
public class Transportistas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idtransportista")
    private int idTransportista;

    @NotNull
    @Column(name = "nombre_fantasia")
    private String nombreFantasia;

    @NotNull
    @Column(name = "nombre_real")
    private String nombreReal;

    @NotNull
    @Column(name = "iddocumento")
    private int idDocumento;

    @NotNull
    @Column(name = "numero")
    private Long numeroDocumento;

    @NotNull
    @OneToOne
    @JoinColumn(name = "iddomicilio")
    private Domicilios idDomicilio;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "situacion_fiscal:: text", write = "?::sfiscal")
    private SituacionFiscalEnum situacionFiscal;

    @Column(name = "telefono_uno")
    private String telefonoUno;

    @Column(name = "telefono_dos")
    private String telefonoDos;

    @Column(name = "telefono_tres")
    private String telefonoTres;

    @Column(name = "email")
    private String email;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "tipo_transporte:: text", write = "?::ttransporte")
    private TipoTransporteEnum tipoTransporte;

      // Relación muchos a muchos con Banco, usando la entidad intermedia ClienteBanco
    @OneToMany(mappedBy = "transportistas", cascade = CascadeType.ALL)
    private Set<TransportistasBancos> transportistasBancos;

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
    public int getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(int idTransportista) {
        this.idTransportista = idTransportista;
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

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public Domicilios getIdDomicilio() {
        return idDomicilio;
    }

    public void setIdDomicilio(Domicilios idDomicilio) {
        this.idDomicilio = idDomicilio;
    }

    public SituacionFiscalEnum getSituacionFiscal() {
        return situacionFiscal;
    }

    public void setSituacionFiscal(SituacionFiscalEnum situacionFiscal) {
        this.situacionFiscal = situacionFiscal;
    }

    public String getTelefonoUno() {
        return telefonoUno;
    }

    public void setTelefonoUno(String telefonoUno) {
        this.telefonoUno = telefonoUno;
    }

    public String getTelefonoDos() {
        return telefonoDos;
    }

    public void setTelefonoDos(String telefonoDos) {
        this.telefonoDos = telefonoDos;
    }

    public String getTelefonoTres() {
        return telefonoTres;
    }

    public void setTelefonoTres(String telefonoTres) {
        this.telefonoTres = telefonoTres;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public TipoTransporteEnum getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(TipoTransporteEnum tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public Long getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(Long numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public Set<TransportistasBancos> getTransportistasBancos() {
        return transportistasBancos;
    }

    public void setTransportistasBancos(Set<TransportistasBancos> transportistasBancos) {
        this.transportistasBancos = transportistasBancos;
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
