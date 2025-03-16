package com.ideadistribuidora.visus.data.enums;

public enum TipoTransporteEnum {
    Terrestre("Terrestre"),
    Marítimo("Marítimo"),
    Fluvial("Fluvial"),
    Aéreo("Aéreo");

    private final String displayName;

    TipoTransporteEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
