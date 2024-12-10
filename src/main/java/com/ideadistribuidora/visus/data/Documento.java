package com.ideadistribuidora.visus.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "documentos")
public class Documento {
    @Id
    @NotNull
    @Column(name = "iddocumento")
    private Integer id_Documento;
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "Documentos [idDocumento=" + id_Documento + ", descripcion=" + descripcion + "]";
    }

    public Integer getId_Documento() {
        return id_Documento;
    }

    public void setId_Documento(Integer id_Documento) {
        this.id_Documento = id_Documento;
    }

}
