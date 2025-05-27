package com.ideadistribuidora.visus.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "pedidos_items")
public class PedidosItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iditem", nullable = false)
    private int idItem;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedidos idPedido;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "idarticulo", nullable = false)
    private Articulos idArticulo;

    @NotNull
    @Column(name = "precio_articulo", nullable = false)
    private BigDecimal precioArticulo;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private BigDecimal cantidad;

    @Column(name = "es_bonificacion")
    private boolean esBonificacion;

    @Column(name = "bonificacion")
    private BigDecimal bonificacion;

    @Column(name = "es_recargo")
    private boolean esRecargo;

    @Column(name = "recargo")
    private BigDecimal recargo;

    @Transient
    private boolean persistBonArt;

    // Getters and Setters
    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public Pedidos getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedidos idPedido) {
        this.idPedido = idPedido;
    }

    public Articulos getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Articulos idArticulo) {
        this.idArticulo = idArticulo;
    }

    public BigDecimal getPrecioArticulo() {
        return precioArticulo;
    }

    public void setPrecioArticulo(BigDecimal precioArticulo) {
        this.precioArticulo = precioArticulo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
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

    public boolean isPersistBonArt() {
        return persistBonArt;
    } 
    
    public void setPersistBonArt(boolean persistBonArt) {
        this.persistBonArt = persistBonArt;
    }
}