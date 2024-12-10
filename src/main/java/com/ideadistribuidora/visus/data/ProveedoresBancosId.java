package com.ideadistribuidora.visus.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ProveedoresBancosId implements Serializable {
    @Column(name = "idproveedor")
    private int idProveedor;
    @Column(name = "idbanco")
    private int idBanco;

    public ProveedoresBancosId() {
    }

    public ProveedoresBancosId(int idProveedor, int idBanco) {
        this.idProveedor = idProveedor;
        this.idBanco = idBanco;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
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
        ProveedoresBancosId that = (ProveedoresBancosId) o;
        return Objects.equals(idProveedor, that.idProveedor) && Objects.equals(idBanco, that.idBanco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProveedor, idBanco);
    }

}
