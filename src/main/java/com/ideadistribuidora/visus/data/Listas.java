package com.ideadistribuidora.visus.data;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "listas")
public class Listas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlista")
    private int idLista;

    @NotNull
    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "lista", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListasPorcentuales> listasPorcentuales = new ArrayList<>();

    // Getters and Setters
    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<ListasPorcentuales> getListasPorcentuales() {
        return listasPorcentuales;
    }

    public void setListasPorcentuales(List<ListasPorcentuales> listasPorcentuales) {
        this.listasPorcentuales = listasPorcentuales;
    }
}
