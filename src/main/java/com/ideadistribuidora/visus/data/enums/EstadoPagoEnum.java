package com.ideadistribuidora.visus.data.enums;

public enum EstadoPagoEnum {
    Pago_Pendiente("PAGO PENDIENTE"),
    Pago_Aprobado("PAGO APROBADO"),
    Pago_Rechazado("PAGO RECHAZADO"),
    Sin_Estado("SIN ESTADO"); 

    private final String estadoPago;

    EstadoPagoEnum(String estadoPago) {
        this.estadoPago = estadoPago;
    }

    public String getEstadoPago() {
        return estadoPago;
    }
}