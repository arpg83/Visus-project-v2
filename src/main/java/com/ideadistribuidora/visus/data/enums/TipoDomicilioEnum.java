package com.ideadistribuidora.visus.data.enums;

public enum TipoDomicilioEnum {
    Fiscal_Pcial_Jurisdicción_Sede("Fiscal Pcial Jurisdicción Sede"),
    Principal_de_Actividades("Principal de Actividades"),
    Fiscal_Jurisdiccional("Fiscal Jurisdiccional"),
    Otros_Domicilios("Otros Domicilios"),
    Sin_actividad("Sin actividad");

    private String displayTipoDomicilio;

    public String getDisplayTipoDomicilio() {
        return displayTipoDomicilio;
    }

    private TipoDomicilioEnum(String displayTipoDomicilio) {
        this.displayTipoDomicilio = displayTipoDomicilio;
    }

}
