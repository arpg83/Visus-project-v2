package com.ideadistribuidora.visus.data.enums;

public enum SituacionFiscalEnum {

    IVA_Responsable_No_Inscripto("IVA Responsable No Inscripto"),
    IVA_Responsable_Inscripto("IVA Responsable Inscripto"),
    IVA_Sujeto_Exento("IVA Sujeto Exento"),
    Consumidor_Final("Consumidor Final"),
    Responsable_Monotributo("Responsable Monotributo"),
    Proveedor_del_Exterior("Proveedor del Exterior"),
    Cliente_del_Exterior("Cliente del Exterior"),
    IVA_Liberado_Ley_19640("IVA Liberado - Ley 19.640"),
    Monotributista_Social("Monotributista Social"),
    IVA_No_Alcanzado("IVA No Alcanzado");

    private String displaySituacionFiscal;

    public String getDisplaySituacionFiscal() {
        return displaySituacionFiscal;
    }

    private SituacionFiscalEnum(String displaySituacionFiscal) {
        this.displaySituacionFiscal = displaySituacionFiscal;
    }

}
