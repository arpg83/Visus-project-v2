package com.ideadistribuidora.visus.data.enums;

public enum VisitaEnum {
    Semanal("Semanal"),
    Quincenal("Quincenal"),
    Mensual("Mensual"),
    Bimestral("Bimestral"),
    Trimestral("Trimestral"),
    Cuatrimestral("Cuatrimestral"),
    Semestral("Semestral"), Anual("Anual");

    private String displayVisita;

    public String getDisplayVisita() {
        return displayVisita;
    }

    private VisitaEnum(String displayVisita) {
        this.displayVisita = displayVisita;
    }

}
