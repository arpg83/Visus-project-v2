package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "coeficientes")
public class Coeficientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcoeficiente")
    private int idCoeficiente;

    @NotNull
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull
    @Column(name = "coeficiente")
    private BigDecimal coeficiente;

    @NotNull
    @Column(name = "cuotas")
    private short cuotas;

    @Transient
    private Date fecha;

    @Transient
    private BigDecimal monto;

    // Getters and Setters
    public int getIdCoeficiente() {
        return idCoeficiente;
    }

    public void setIdCoeficiente(int idCoeficiente) {
        this.idCoeficiente = idCoeficiente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public BigDecimal getCoeficiente() {
        return coeficiente;
    }

    public void setCoeficiente(BigDecimal coeficiente) {
        this.coeficiente = coeficiente;
    }

    public short getCuotas() {
        return cuotas;
    }

    public void setCuotas(short cuotas) {
        this.cuotas = cuotas;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigDecimal getMonto() {
        return monto;
    } 

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    
}
