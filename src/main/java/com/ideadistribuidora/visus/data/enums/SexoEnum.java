package com.ideadistribuidora.visus.data.enums;

public enum SexoEnum {
    MASCULINO("Masculino"),
    FEMENINO("Femenino"),
    NO_INFORMA("No Informa"),
    NO_CORRESPONDE("No Corresponde");

    private String displaySexo;

    SexoEnum(String displaySexo) {
        this.displaySexo = displaySexo;
    }

    public String getDisplaySexo() {
        return displaySexo;
    }

    @Override
    public String toString() {
        return displaySexo;
    }

}
