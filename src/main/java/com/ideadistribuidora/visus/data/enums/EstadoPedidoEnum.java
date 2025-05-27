package com.ideadistribuidora.visus.data.enums;

public enum EstadoPedidoEnum {
    Aceptado("ACEPTADO"),
    Cancelado("CANCELADO"),
    Verificando_Factura("VERIFICANDO FACTURA"),
    Facturado("FACTURADO"),
    En_Picking ("EN PICKING"),
    Enviado ("ENVIADO");


    private String estadoPedido;

    EstadoPedidoEnum(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }
}