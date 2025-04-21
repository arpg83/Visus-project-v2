package com.ideadistribuidora.visus.data.enums;

public enum PlataformaEnum {
    WEB("WEB"),
    APP("APP");

    private final String plataforma;

    PlataformaEnum(String plataforma) {
        this.plataforma = plataforma;
    }

    public String getPlataforma() {
        return plataforma;
    }
}