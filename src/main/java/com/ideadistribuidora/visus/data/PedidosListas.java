package com.ideadistribuidora.visus.data;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pedidos_listas")
public class PedidosListas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpedidoslistas", nullable = false)
    private int idPedidoListas;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idpedido", nullable = false)
    private Pedidos idPedido;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "idlista", nullable = false)
    private ListasPorcentuales idListas;

    // Getters and Setters
    public int getIdPedidoListas() {
        return idPedidoListas;
    }

    public void setIdPedidoListas(int idPedidoListas) {
        this.idPedidoListas = idPedidoListas;
    }

    public Pedidos getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Pedidos idPedido) {
        this.idPedido = idPedido;
    }

    public ListasPorcentuales getIdListas() {
        return idListas;
    }

    public void setIdListas(ListasPorcentuales idListas) {
        this.idListas = idListas;
    }
}