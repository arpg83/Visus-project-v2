package com.ideadistribuidora.visus.data.enums;

public enum TipoCuentaEnum {
    CA_$("Caja de Ahorro en Pesos"),
    CA_U$S("Caja de Ahorro en Dolares"),
    CC("Cuenta Corriente");

    private String displayTipoDeCuenta;

    public String getDisplayTipoDeCuenta() {
        return displayTipoDeCuenta;
    }

    private TipoCuentaEnum(String displayTipoDeCuenta) {
        this.displayTipoDeCuenta = displayTipoDeCuenta;
    }

}
