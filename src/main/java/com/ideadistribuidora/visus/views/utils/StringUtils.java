package com.ideadistribuidora.visus.views.utils;

public class StringUtils {
    public static String separarPalabras(String input) {
        // Expresión regular que encuentra mayúsculas seguidas por minúsculas o el final
        // de la cadena
        String regex = "(?=[A-Z])";

        // Divide el string usando la expresión regular y une con espacios
        String[] resultado = input.split(regex);
        return String.join(" ", resultado);
    }

}
