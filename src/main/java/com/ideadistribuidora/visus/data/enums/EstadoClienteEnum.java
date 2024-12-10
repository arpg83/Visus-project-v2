package com.ideadistribuidora.visus.data.enums;

public enum EstadoClienteEnum {
    Habilitado("Habilitado"),
    Deshabilitado("Deshabilitado"),
    Baja("Baja");

    // HABILITADO,
    // INACTIVO,
    // SUSPENDIDO

    private String displayEstadoCliente;

    public String getDisplayEstadoCliente() {
        return displayEstadoCliente;
    }

    private EstadoClienteEnum(String displayEstadoCliente) {
        this.displayEstadoCliente = displayEstadoCliente;
    }

    // Método para convertir de String a EstadoClienteEnum
    // public static EstadoClienteEnum fromString(String displayString) {
    // for (EstadoClienteEnum estado : EstadoClienteEnum.values()) {
    // if (estado.getDisplayEstadoCliente().equalsIgnoreCase(displayString)) {
    // return estado;
    // }
    // }
    // throw new IllegalArgumentException("No se encontró un EstadoClienteEnum para
    // el valor: " + displayString);
    // }

}
