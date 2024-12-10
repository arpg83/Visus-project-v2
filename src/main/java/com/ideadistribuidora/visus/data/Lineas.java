package com.ideadistribuidora.visus.data;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "lineas")
public class Lineas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlineas")
    private int idLineas;
    @Column(name = "descripcion")
    private String descripcion;
    @ManyToOne
    @JoinColumn(name = "idrubros", nullable = false)
    private Rubros rubros;

    public int getIdLineas() {
        return idLineas;
    }

    public void setIdLineas(int idLineas) {
        this.idLineas = idLineas;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Rubros getRubros() {
        return rubros;
    }

    public void setRubros(Rubros rubros) {
        this.rubros = rubros;
    }

}
