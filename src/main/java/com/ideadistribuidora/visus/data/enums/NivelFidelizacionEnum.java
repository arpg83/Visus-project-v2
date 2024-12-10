package com.ideadistribuidora.visus.data.enums;

public enum NivelFidelizacionEnum {

    Compra_Ocasional("Compra Ocasional"),
    Compra_Regular("Compra Regular"),
    Compra_Frecuente("Compra Frecuente");

    private String displayNivelFidelizacion;

    NivelFidelizacionEnum(String displayNivelFidelizacion) {
        this.displayNivelFidelizacion = displayNivelFidelizacion;
    }

    public String getDisplayNivelFidelizacion() {
        return displayNivelFidelizacion;
    }

    @Override
    public String toString() {
        return displayNivelFidelizacion;
    }

}
