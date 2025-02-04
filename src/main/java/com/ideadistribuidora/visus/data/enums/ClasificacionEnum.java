package com.ideadistribuidora.visus.data.enums;

public enum ClasificacionEnum {
    Bonificación("Bonificación"),
    Recargo("Recargo");

    private final String displayName;

    ClasificacionEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
