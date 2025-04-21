package com.ideadistribuidora.visus.data.enums;

public enum EstadoPagoEnum {
    PAGO_PENDIENTE("PAGO PENDIENTE"),
    PAGO_APROBADO("PAGO APROBADO"),
    PAGO_RECHAZADO("PAGO RECHAZADO"),
    SIN_ESTADO("SIN ESTADO");

    private final String estadoPago;

    EstadoPagoEnum(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }
}