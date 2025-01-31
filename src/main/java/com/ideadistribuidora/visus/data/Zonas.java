package com.ideadistribuidora.visus.data;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.VisitaEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "zonas")
public class Zonas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idzona")
    private int idZona;
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;
    @NotNull
    @Column(name = "area_geografica")
    private String areaGeografica;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "frecuencia_visita:: text", write = "?::fvisita")
    private VisitaEnum frecuenciaVisita;

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getAreaGeografica() {
        return areaGeografica;
    }

    public void setAreaGeografica(String areaGeografica) {
        this.areaGeografica = areaGeografica;
    }

    public VisitaEnum getFrecuenciaVisita() {
        return frecuenciaVisita;
    }

    public void setFrecuenciaVisita(VisitaEnum frecuenciaVisita) {
        this.frecuenciaVisita = frecuenciaVisita;
    }

}
