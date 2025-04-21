package com.ideadistribuidora.visus.data;

import com.ideadistribuidora.visus.data.enums.EstadoPagoEnum;
import com.ideadistribuidora.visus.data.enums.EstadoPedidoEnum;
import com.ideadistribuidora.visus.data.enums.PlataformaEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pedidos")
public class Pedidos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpedido", nullable = false)
    private int idPedido;

    @Column(name = "es_bonificacion")
    private boolean esBonificacion;

    @Column(name = "bonificacion")
    private BigDecimal bonificacion;

    @Column(name = "es_recargo")
    private boolean esRecargo;

    @Column(name = "recargo")
    private BigDecimal recargo;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idcliente", nullable = false)
    private Clientes idCliente;

    @NotNull
    @Column(name = "fecha_pedido", nullable = false)
    private LocalDate fechaPedido;

    @Column(name = "fecha_modificacion")
    private LocalDate fechaModificacion;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @Column(name = "fecha_anulacion")
    private LocalDate fechaAnulacion;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pedido", nullable = false)
    private EstadoPedidoEnum estadoPedido;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "estado_pago", nullable = false)
    private EstadoPagoEnum estadoPago;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "plataforma", nullable = false)
    private PlataformaEnum plataforma;

    @Column(name = "nota_al_pie")
    private String notaAlPie;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idvendedor", nullable = false)
    private Vendedores idVendedor;

    @Column(name = "domicilio_cliente", nullable = false)
    private String domicilioClienteString;

    @Transient
    @NotNull
    private Domicilios domicilioCliente;

    @Transient
    @NotNull
    private String estadoPedidoString;

    // Getters and Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public boolean isEsBonificacion() {
        return esBonificacion;
    }

    public void setEsBonificacion(boolean esBonificacion) {
        this.esBonificacion = esBonificacion;
    }

    public BigDecimal getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
    }

    public boolean isEsRecargo() {
        return esRecargo;
    }

    public void setEsRecargo(boolean esRecargo) {
        this.esRecargo = esRecargo;
    }

    public BigDecimal getRecargo() {
        return recargo;
    }

    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }

    public Clientes getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Clientes idCliente) {
        this.idCliente = idCliente;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDate fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDate getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(LocalDate fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public LocalDate getFechaAnulacion() {
        return fechaAnulacion;
    }

    public void setFechaAnulacion(LocalDate fechaAnulacion) {
        this.fechaAnulacion = fechaAnulacion;
    }

    public EstadoPedidoEnum getEstadoPedido() {
        return estadoPedido;
    }

    public void setEstadoPedido(EstadoPedidoEnum estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public EstadoPagoEnum getEstadoPago() {
        return estadoPago;
    }

    public void setEstadoPago(EstadoPagoEnum estadoPago) {
        this.estadoPago = estadoPago;
    }

    public PlataformaEnum getPlataforma() {
        return plataforma;
    }

    public void setPlataforma(PlataformaEnum plataforma) {
        this.plataforma = plataforma;
    }

    public String getNotaAlPie() {
        return notaAlPie;
    }

    public void setNotaAlPie(String notaAlPie) {
        this.notaAlPie = notaAlPie;
    }

    public Vendedores getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Vendedores idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getEstadoPedidoString() {
        return estadoPedidoString;
    }

    public void setEstadoPedidoString(String estadoPedidoString) {
        this.estadoPedidoString = estadoPedidoString;
    }

    public String getDomicilioClienteString() {
        return domicilioClienteString;
    }

    public void setDomicilioClienteString(String domicilioClienteString) {
        this.domicilioClienteString = domicilioClienteString;
    }

    public Domicilios getDomicilioCliente() {
        return domicilioCliente;
    }

    public void setDomicilioCliente(Domicilios domicilioCliente) {
        this.domicilioCliente = domicilioCliente;
    }
    
    
}