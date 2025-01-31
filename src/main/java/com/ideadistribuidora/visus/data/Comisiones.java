package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.TipoComisionEnum;

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
@Table(name = "comisiones")
public class Comisiones {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcomision")
    private int idComision;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "tipo_comision:: text", write = "?::tcomision")
    private TipoComisionEnum tipoComision;
    @NotNull
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;
    @NotNull
    @Column(name = "vigencia_desde")
    private LocalDateTime vigenciaDesde;
    @Column(name = "vigencia_hasta")
    private LocalDateTime vigenciaHasta;
    @NotNull
    @Column(name = "porcentaje_sobre_importe")
    private BigDecimal porcentajeSobreImporte;
    @Column(name = "porcentaje_importe_fijo")
    private BigDecimal porcentajeImporteFijo;
    @Column(name = "porcentaje_sobre_margen")
    private BigDecimal porcentajeSobreMargen;

    public int getIdComision() {
        return idComision;
    }

    public void setIdComision(int idComision) {
        this.idComision = idComision;
    }

    public TipoComisionEnum getTipoComision() {
        return tipoComision;
    }

    public void setTipoComision(TipoComisionEnum tipoComision) {
        this.tipoComision = tipoComision;
    }


    public BigDecimal getPorcentajeSobreImporte() {
        return porcentajeSobreImporte;
    }

    public void setPorcentajeSobreImporte(BigDecimal porcentajeSobreImporte) {
        this.porcentajeSobreImporte = porcentajeSobreImporte;
    }

    public BigDecimal getPorcentajeSobreMargen() {
        return porcentajeSobreMargen;
    }

    public void setPorcentajeSobreMargen(BigDecimal porcentajeSobreMargen) {
        this.porcentajeSobreMargen = porcentajeSobreMargen;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getVigenciaDesde() {
        return vigenciaDesde;
    }

    public void setVigenciaDesde(LocalDateTime vigenciaDesde) {
        this.vigenciaDesde = vigenciaDesde;
    }

    public LocalDateTime getVigenciaHasta() {
        return vigenciaHasta;
    }

    public void setVigenciaHasta(LocalDateTime vigenciaHasta) {
        this.vigenciaHasta = vigenciaHasta;
    }

    public BigDecimal getPorcentajeImporteFijo() {
        return porcentajeImporteFijo;
    }

    public void setPorcentajeImporteFijo(BigDecimal porcentajeImporteFijo) {
        this.porcentajeImporteFijo = porcentajeImporteFijo;
    }

    


}
