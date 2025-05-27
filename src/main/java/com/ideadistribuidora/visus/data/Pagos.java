package com.ideadistribuidora.visus.data;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.EstadoPagoEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "pagos")
public class Pagos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpago")
    private int idPago;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idpedido", referencedColumnName = "idPedido")
    private Pedidos idPedido;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "estado:: text", write = "?::epago")
    private EstadoPagoEnum estado;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idformaspago", referencedColumnName = "idFormasPago")
    private FormasDePago idFormasPago;
    public int getIdPago() {
        return idPago;
    }
    public void setIdPago(int idPago) {
        this.idPago = idPago;
    }
    public Pedidos getIdPedido() {
        return idPedido;
    }
    public void setIdPedido(Pedidos idPedido) {
        this.idPedido = idPedido;
    }
    public EstadoPagoEnum getEstado() {
        return estado;
    }
    public void setEstado(EstadoPagoEnum estado) {
        this.estado = estado;
    }
    public FormasDePago getIdFormasPago() {
        return idFormasPago;
    }
    public void setIdFormasPago(FormasDePago idFormasPago) {
        this.idFormasPago = idFormasPago;
    }

    

}
