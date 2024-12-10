package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.EstadoClienteEnum;
import com.ideadistribuidora.visus.data.enums.NivelFidelizacionEnum;
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
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "clientes")
public class Clientes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcliente")
    private int idCliente;
    @NotNull
    @Column(name = "nombre_fantasia")
    private String nombreFantasia;
    @NotNull
    @Column(name = "nombre_cliente")
    private String nombreCliente;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "clientes_domicilios", joinColumns = @JoinColumn(name = "idcliente"), inverseJoinColumns = @JoinColumn(name = "iddomicilio"))
    private Set<Domicilios> domicilios;
    @Column(name = "telefono_secundario")
    private String telefonoSecundario;
    @Column(name = "telefono_movil")
    private String telefonoMovil;
    @Column(name = "otro_telefono")
    private String otroTelefono;
    @NotNull
    @Column(name = "iddocumento")
    private int tipoDeDocumento;
    @NotNull
    @Column(name = "numero")
    private Long numeroDeDocumento;
    @Column(name = "email")
    private String email;
    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;
    @NotNull
    @Column(name = "fecha_ingreso")
    private LocalDate fechaIngreso;
    @NotNull
    @Column(name = "fecha_ultimacompra")
    private LocalDate fechaUltimaCompra;
    @NotNull
    @Column(name = "limite_facturasvencidas")
    private short limiteFacturasVencidas;
    @Column(name = "limite_credito")
    private BigDecimal limiteCredito;
    @Column(name = "pago_minimo")
    private BigDecimal pagoMinimo;
    @Column(name = "periodo_carencia")
    private int periodoCarencia;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "nivel_fidelizacion:: text", write = "?::nfidelizacion")
    private NivelFidelizacionEnum nivelFidelizacion;

    // Relación muchos a muchos con Banco, usando la entidad intermedia ClienteBanco
    @OneToMany(mappedBy = "clientes", cascade = CascadeType.ALL)
    private Set<ClientesBancos> clientesBancos;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "situacion_fiscal:: text", write = "?::sfiscal")
    private SituacionFiscalEnum situacionFiscal;
    @NotNull
    @Column(name = "saldo_ctacte")
    private BigDecimal saldoCuentaCorriente;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "estado_cliente:: text", write = "?::ecliente")
    private EstadoClienteEnum estadoCliente;
    @Column(name = "regente")
    private Boolean regente;
    @Column(name = "matricula")
    private String matricula;
    @Column(name = "observaciones")
    private String observaciones;

    public String getNombreFantasia() {
        return nombreFantasia;
    }

    public void setNombreFantasia(String nombreFantasia) {
        this.nombreFantasia = nombreFantasia;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public void setTelefonoMovil(String telefonoMovil) {
        this.telefonoMovil = telefonoMovil;
    }

    public String getTelefonoSecundario() {
        return telefonoSecundario;
    }

    public void setTelefonoSecundario(String telefonoSecundario) {
        this.telefonoSecundario = telefonoSecundario;
    }

    public String getOtroTelefono() {
        return otroTelefono;
    }

    public void setOtroTelefono(String otroTelefono) {
        this.otroTelefono = otroTelefono;
    }

    public int getTipoDeDocumento() {
        return tipoDeDocumento;
    }

    public void setTipoDeDocumento(int tipoDeDocumento) {
        this.tipoDeDocumento = tipoDeDocumento;
    }

    public Long getNumeroDeDocumento() {
        return numeroDeDocumento;
    }

    public void setNumeroDeDocumento(Long numeroDeDocumento) {
        this.numeroDeDocumento = numeroDeDocumento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(LocalDate fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public LocalDate getFechaUltimaCompra() {
        return fechaUltimaCompra;
    }

    public void setFechaUltimaCompra(LocalDate fechaUltimaCompra) {
        this.fechaUltimaCompra = fechaUltimaCompra;
    }

    public short getLimiteFacturasVencidas() {
        return limiteFacturasVencidas;
    }

    public void setLimiteFacturasVencidas(short limiteFacturasVencidas) {
        this.limiteFacturasVencidas = limiteFacturasVencidas;
    }

    public BigDecimal getLimiteCredito() {
        return limiteCredito;
    }

    public void setLimiteCredito(BigDecimal limiteCredito) {
        this.limiteCredito = limiteCredito;
    }

    public BigDecimal getPagoMinimo() {
        return pagoMinimo;
    }

    public void setPagoMinimo(BigDecimal pagoMinimo) {
        this.pagoMinimo = pagoMinimo;
    }

    public int getPeriodoCarencia() {
        return periodoCarencia;
    }

    public void setPeriodoCarencia(int periodoCarencia) {
        this.periodoCarencia = periodoCarencia;
    }

    public BigDecimal getSaldoCuentaCorriente() {
        return saldoCuentaCorriente;
    }

    public void setSaldoCuentaCorriente(BigDecimal saldoCuentaCorriente) {
        this.saldoCuentaCorriente = saldoCuentaCorriente;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public NivelFidelizacionEnum getNivelFidelizacion() {
        return nivelFidelizacion;
    }

    public void setNivelFidelizacion(NivelFidelizacionEnum nivelFidelizacion) {
        this.nivelFidelizacion = nivelFidelizacion;
    }

    public SituacionFiscalEnum getSituacionFiscal() {
        return situacionFiscal;
    }

    public void setSituacionFiscal(SituacionFiscalEnum situacionFiscal) {
        this.situacionFiscal = situacionFiscal;
    }

    public EstadoClienteEnum getEstadoCliente() {
        return estadoCliente;
    }

    public void setEstadoCliente(EstadoClienteEnum estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public Set<Domicilios> getDomicilios() {
        return domicilios;
    }

    public void setDomicilios(Set<Domicilios> domicilios) {
        this.domicilios = domicilios;
    }

    public Set<ClientesBancos> getClientesBancos() {
        return clientesBancos;
    }

    public void setClientesBancos(Set<ClientesBancos> clientesBancos) {
        this.clientesBancos = clientesBancos;
    }

    public Boolean getRegente() {
        return regente;
    }

    public void setRegente(Boolean regente) {
        this.regente = regente;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

}
