package com.ideadistribuidora.visus.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alicuotas")
public class Alicuotas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idalicuota")
    private int idAlicuota;
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;

    public int getIdAlicuota() {
        return idAlicuota;
    }

    public void setIdAlicuota(int idAlicuota) {
        this.idAlicuota = idAlicuota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
