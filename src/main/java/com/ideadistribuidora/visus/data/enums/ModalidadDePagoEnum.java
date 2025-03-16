package com.ideadistribuidora.visus.data.enums;

public enum ModalidadDePagoEnum {
    Efectivo("Efectivo"), 
    Transferencia_Bancaria("Transferencia Bancaria"), 
    Tarjeta_de_Crédito("Tarjeta de Crédito"), 
    Tarjeta_de_Débito("Tarjeta de Débito"), 
    Cheque("Cheque");

    private final String modalidadDePago;  

    private ModalidadDePagoEnum(String modalidadDePago) {
        this.modalidadDePago = modalidadDePago;
    }

    public String getModalidadDePago() {
        return modalidadDePago;
    }
    
       

}
