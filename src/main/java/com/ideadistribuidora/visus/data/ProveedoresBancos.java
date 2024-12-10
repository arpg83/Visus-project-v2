package com.ideadistribuidora.visus.data;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "proveedores_bancos")
public class ProveedoresBancos {
    @EmbeddedId
    private ProveedoresBancosId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idProveedor")
    @JoinColumn(name = "idproveedor")
    private Proveedores proveedores;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("idBanco")
    @JoinColumn(name = "idbanco")
    private Bancos bancos;

    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "tipo_cuenta:: text", write = "?::tcuenta")
    private TipoCuentaEnum tipoCuenta;

    @NotNull
    @Column(name = "cbu")
    private Long cbu;

    @Column(name = "alias")
    private String alias;

    public ProveedoresBancosId getId() {
        return id;
    }

    public void setId(ProveedoresBancosId id) {
        this.id = id;
    }

    public Proveedores getProveedores() {
        return proveedores;
    }

    public void setProveedores(Proveedores proveedores) {
        this.proveedores = proveedores;
    }

    public Bancos getBancos() {
        return bancos;
    }

    public void setBancos(Bancos bancos) {
        this.bancos = bancos;
    }

    public TipoCuentaEnum getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(TipoCuentaEnum tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Long getCbu() {
        return cbu;
    }

    public void setCbu(Long cbu) {
        this.cbu = cbu;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

}
