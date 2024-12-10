package com.ideadistribuidora.visus.data.enums;

public enum ProvinciasEnum {
    BUENOS_AIRES("Buenos Aires"),
    CABA("Ciudad Autónoma de Buenos Aires"),
    CATAMARCA("Catamarca"),
    CHACO("Chaco"),
    CHUBUT("Chubut"),
    CORDOBA("Córdoba"),
    CORRIENTES("Corrientes"),
    ENTRE_RIOS("Entre Ríos"),
    FORMOSA("Formosa"),
    JUJUY("Jujuy"),
    PAMPA("La Pampa"),
    RIOJA("La Rioja"),
    MENDOZA("Mendoza"),
    MISIONES("Misiones"),
    NEUQUEN("Neuquén"),
    RIO_NEGRO("Río Negro"),
    SALTA("Salta"),
    SAN_JUAN("San Juan"),
    SAN_LUIS("San Luis"),
    SANTA_CRUZ("Santa Cruz"),
    SANTA_FE("Santa Fe"),
    SANT_ESTERO("Santiago del Estero"),
    TIERRA_FUEGO("Tierra del Fuego"),
    ANT_ISL_ATL_SUR("Antártida e Islas del Atlántico Sur"),
    TUCUMAN("Tucumán");

    private String displayProvincia;

    public String getDisplayProvincia() {
        return displayProvincia;
    }

    private ProvinciasEnum(String displayProvincia) {
        this.displayProvincia = displayProvincia;
    }

}
