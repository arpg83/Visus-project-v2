package com.ideadistribuidora.visus.data.enums;

public enum TipoArticuloEnum {
    Normal("Normal"),
    Consignación("Consignación"),
    Oferta("Oferta");

    private String displayTipoArticulo;

    TipoArticuloEnum(String displayTipoArticulo) {
        this.displayTipoArticulo = displayTipoArticulo;
    }

    public String getDisplayTipoArticulo() {
        return displayTipoArticulo;
    }

}
