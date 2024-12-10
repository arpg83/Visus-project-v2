package com.ideadistribuidora.visus.data.enums;

public enum EstadoCivilEnum {
    SOLTERO("Soltero"),
    CASADO("Casado"),
    SEPAREADO("Separado"),
    Divorciado("Divorciado"),
    VIUDO("Viudo"),
    NO_APLICA("No Aplica");

    private String displayEstadoCivil;

    EstadoCivilEnum(String displayEstadoCivil) {
        this.displayEstadoCivil = displayEstadoCivil;
    }

    public String getDisplayEstadoCivil() {
        return displayEstadoCivil;
    }

}
