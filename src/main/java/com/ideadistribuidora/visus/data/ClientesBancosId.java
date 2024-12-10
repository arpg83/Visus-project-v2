package com.ideadistribuidora.visus.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ClientesBancosId implements Serializable {
    @Column(name = "idcliente")
    private int idCliente;
    @Column(name = "idbanco")
    private int idBanco;

    public ClientesBancosId() {
    }

    public ClientesBancosId(int idCliente, int idBanco) {
        this.idCliente = idCliente;
        this.idBanco = idBanco;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ClientesBancosId that = (ClientesBancosId) o;
        return Objects.equals(idCliente, that.idCliente) && Objects.equals(idBanco, that.idBanco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCliente, idBanco);
    }

}
