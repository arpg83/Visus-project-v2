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
@Table(name = "listas_porcentuales")
public class ListasPorcentuales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idlis_porc", nullable = false)
    private int idLisPorc;

    @ManyToOne
    @JoinColumn(name = "idlista", nullable = false)
    private Listas lista;

    @ManyToOne
    @JoinColumn(name = "idporcentual", nullable = false)
    private Porcentuales porcentual;

    // Getters and Setters
    public int getIdLisPorc() {
        return idLisPorc;
    }

    public void setIdLisPorc(int idLisPorc) {
        this.idLisPorc = idLisPorc;
    }

    public Listas getLista() {
        return lista;
    }

    public void setLista(Listas lista) {
        this.lista = lista;
    }

    public Porcentuales getPorcentual() {
        return porcentual;
    }

    public void setPorcentual(Porcentuales porcentual) {
        this.porcentual = porcentual;
    }
}