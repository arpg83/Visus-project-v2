package com.ideadistribuidora.visus.data.enums;

public enum EstadoArticuloEnum {
    En_Existencias("En Existencias"),
    Disponible("Disponible"),
    Comprometido("Comprometido"),
    No_Disponible("No Disponible"),
    Entrantes("Entrantes");

    private String displayEstadoArticulo;

    EstadoArticuloEnum(String displayEstadoArticulo) {
        this.displayEstadoArticulo = displayEstadoArticulo;
    }

    public String getDisplayEstadoArticulo() {
        return displayEstadoArticulo;
    }

}
