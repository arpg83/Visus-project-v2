package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.ClasificacionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "porcentuales")
public class Porcentuales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idporcentual", nullable = false)
    private int idPorcentual;

    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "porcentual", nullable = false)
    private BigDecimal porcentual;

    @Column(name = "inicio_vigencia", nullable = false)
    private LocalDate inicioVigencia;

    @Column(name = "fin_vigencia")
    private LocalDate finVigencia;

    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "clasificacion:: text", write = "?::tporcentual")
    private ClasificacionEnum clasificacion;

     @OneToMany(mappedBy = "porcentual", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListasPorcentuales> listasPorcentuales = new ArrayList<>();

    // Getters and setters

    public int getIdPorcentual() {
        return idPorcentual;
    }

    public void setIdPorcentual(int idPorcentual) {
        this.idPorcentual = idPorcentual;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getPorcentual() {
        return porcentual;
    }

    public void setPorcentual(BigDecimal porcentual) {
        this.porcentual = porcentual;
    }

    public LocalDate getInicioVigencia() {
        return inicioVigencia;
    }

    public void setInicioVigencia(LocalDate inicioVigencia) {
        this.inicioVigencia = inicioVigencia;
    }

    public LocalDate getFinVigencia() {
        return finVigencia;
    }

    public void setFinVigencia(LocalDate finVigencia) {
        this.finVigencia = finVigencia;
    }

    public ClasificacionEnum getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(ClasificacionEnum clasificacion) {
        this.clasificacion = clasificacion;
    }

    public List<ListasPorcentuales> getListasPorcentuales() {
        return listasPorcentuales;
    }

    public void setListasPorcentuales(List<ListasPorcentuales> listasPorcentuales) {
        this.listasPorcentuales = listasPorcentuales;
    }

    
}
