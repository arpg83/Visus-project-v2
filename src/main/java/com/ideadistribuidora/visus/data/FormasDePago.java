package com.ideadistribuidora.visus.data;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnTransformer;
import com.ideadistribuidora.visus.data.enums.ModalidadDePagoEnum;
import java.math.BigDecimal;

@Entity
@Table(name = "formasdepago")
public class FormasDePago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idformaspago")
    private int idFormasPago;

    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "modalidad::text", write = "?::mpago")
    private ModalidadDePagoEnum modalidad;

    @ManyToOne
    @JoinColumn(name = "idcoeficiente", nullable = false)
    private Coeficientes idCoeficiente;

    @Column(name = "es_dto_pronto_pago")
    private boolean esDtoProntoPago;

    @Column(name = "dto_pronto_pago")
    private BigDecimal dtoProntoPago;

    @Column(name = "es_meses_completos")
    private boolean mesesCompletos;

    @Transient
    private String coeficienteDesc;
    @Transient
    private BigDecimal coeficiente;
    @Transient
    private short cuotas;

    // Getters and Setters
    public int getIdFormasPago() {
        return idFormasPago;
    }

    public void setIdFormasPago(int idFormasPago) {
        this.idFormasPago = idFormasPago;
    }

    public ModalidadDePagoEnum getModalidad() {
        return modalidad;
    }

    public void setModalidad(ModalidadDePagoEnum modalidad) {
        this.modalidad = modalidad;
    }

    public Coeficientes getIdCoeficiente() {
        return idCoeficiente;
    }

    public void setIdCoeficiente(Coeficientes idCoeficiente) {
        this.idCoeficiente = idCoeficiente;
    }

    public boolean isEsDtoProntoPago() {
        return esDtoProntoPago;
    }

    public void setEsDtoProntoPago(boolean esDtoProntoPago) {
        this.esDtoProntoPago = esDtoProntoPago;
    }

    public BigDecimal getDtoProntoPago() {
        return dtoProntoPago;
    }

    public void setDtoProntoPago(BigDecimal dtoProntoPago) {
        this.dtoProntoPago = dtoProntoPago;
    }

    public boolean isMesesCompletos() {
        return mesesCompletos;
    }

    public void setMesesCompletos(boolean mesesCompletos) {
        this.mesesCompletos = mesesCompletos;
    }

    public String getCoeficienteDesc() {
        return coeficienteDesc;
    }

    public void setCoeficienteDesc(String coeficienteDesc) {
        this.coeficienteDesc = coeficienteDesc;
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

    
}
