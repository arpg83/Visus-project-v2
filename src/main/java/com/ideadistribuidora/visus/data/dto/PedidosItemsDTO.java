package com.ideadistribuidora.visus.data.dto;

import java.math.BigDecimal;

public class PedidosItemsDTO {
    private String articulo;
    private BigDecimal cantidad;
    private String alicuotaDesc;
    private BigDecimal precioUnitario;
    private BigDecimal bonificacion;
    private BigDecimal recargo;
    private BigDecimal subTotalSinImp;
    private BigDecimal subTotalConImp;

    
    public PedidosItemsDTO(String articulo, BigDecimal cantidad, String alicuotaDesc, BigDecimal precioUnitario,
            BigDecimal bonificacion, BigDecimal recargo, BigDecimal subTotalSinImp, BigDecimal subTotalConImp) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.alicuotaDesc = alicuotaDesc;
        this.precioUnitario = precioUnitario;
        this.bonificacion = bonificacion;
        this.recargo = recargo;
        this.subTotalSinImp = subTotalSinImp;
        this.subTotalConImp = subTotalConImp;
    }
    public String getArticulo() {
        return articulo;
    }
    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }
    public BigDecimal getCantidad() {
        return cantidad;
    }
    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }
    
    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
    public void setPrecioUnitario(BigDecimal precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
    public BigDecimal getBonificacion() {
        return bonificacion;
    }
    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
    }
    public BigDecimal getRecargo() {
        return recargo;
    }
    public void setRecargo(BigDecimal recargo) {
        this.recargo = recargo;
    }
    public BigDecimal getSubTotalSinImp() {
        return subTotalSinImp;
    }
    public void setSubTotalSinImp(BigDecimal subTotalSinImp) {
        this.subTotalSinImp = subTotalSinImp;
    }
    public BigDecimal getSubTotalConImp() {
        return subTotalConImp;
    }
    public void setSubTotalConImp(BigDecimal subTotalConImp) {
        this.subTotalConImp = subTotalConImp;
    }
    public String getAlicuotaDesc() {
        return alicuotaDesc;
    }
    public void setAlicuotaDesc(String alicuotaDesc) {
        this.alicuotaDesc = alicuotaDesc;
    }


    

}
