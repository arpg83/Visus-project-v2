package com.ideadistribuidora.visus.data.enums;

public enum EstadoPedidoEnum {
    ACEPTADO("ACEPTADO"),
    CANCELADO("CANCELADO"),
    VERIFICANDO_FACTURA("VERIFICANDO FACTURA"),
    FACTURADO("FACTURADO"),
    EN_PICKING ("EN PICKING"),
    ENVIADO ("ENVIADO");

    private String estadoPedido;

    EstadoPedidoEnum(String estadoPedido) {
        this.estadoPedido = estadoPedido;
    }

    public String getEstadoPedido() {
        return estadoPedido;
    }
}