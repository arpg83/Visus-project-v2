package com.ideadistribuidora.visus.data.enums;

public enum TipoComisionEnum {
    Directa("Directa"), 
    Fija("Fija"), 
    Escalonada("Escalonada");

    private String displayTipoComision;

    private TipoComisionEnum(String displayTipoComision) {
        this.displayTipoComision = displayTipoComision;
    }

    public String getDisplayTipoComision() {
        return displayTipoComision;
    }

    

}
