package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "comisiones_tramos")
public class ComisionesTramos{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomisiontramo")
    private int idComisionTramo;

    @NotNull
    @Column(name = "idvendedor")
    private int idVendedor;

    @NotNull
    @Column(name = "desde_importe")
    private BigDecimal desdeImporte;

    @NotNull
    @Column(name = "hasta_importe")
    private BigDecimal hastaImporte;

    @NotNull
    @Column(name = "porcentaje")
    private BigDecimal porcentaje;

    // Getters and Setters
    public int getIdComisionTramo() {
        return idComisionTramo;
    }

    public void setIdComisionTramo(int idComisionTramo) {
        this.idComisionTramo = idComisionTramo;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public BigDecimal getDesdeImporte() {
        return desdeImporte;
    }

    public void setDesdeImporte(BigDecimal desdeImporte) {
        this.desdeImporte = desdeImporte;
    }

    public BigDecimal getHastaImporte() {
        return hastaImporte;
    }

    public void setHastaImporte(BigDecimal hastaImporte) {
        this.hastaImporte = hastaImporte;
    }

    public BigDecimal getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(BigDecimal porcentaje) {
        this.porcentaje = porcentaje;
    }
}
