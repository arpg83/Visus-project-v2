package com.ideadistribuidora.visus.data;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;

public class TransportistasBancosId implements Serializable {
     @Column(name = "idtransportista")
    private int idTransportista;
    @Column(name = "idbanco")
    private int idBanco;

    public TransportistasBancosId() {
    }

    public TransportistasBancosId(int idTransportista, int idBanco) {
        this.idTransportista = idTransportista;
        this.idBanco = idBanco;
    }

    public int getIdTransportista() {
        return idTransportista;
    }

    public void setIdTransportista(int idTransportista) {
        this.idTransportista = idTransportista;
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
        TransportistasBancosId that = (TransportistasBancosId) o;
        return Objects.equals(idTransportista, that.idTransportista) && Objects.equals(idBanco, that.idBanco);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTransportista, idBanco);
    }

}
