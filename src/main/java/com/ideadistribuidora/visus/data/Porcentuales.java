package com.ideadistribuidora.visus.data;

import com.ideadistribuidora.visus.data.enums.ClasificacionEnum;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.ColumnTransformer;

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
}
