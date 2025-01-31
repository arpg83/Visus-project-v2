package com.ideadistribuidora.visus.views.utils;

import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class ShortField extends TextField {

    public ShortField(String label, boolean required) {
        super(label);
        setValueChangeMode(ValueChangeMode.EAGER);
        setMaxLength(6); // Maximum length including negative sign
        setRequiredIndicatorVisible(required);

        // Add a value change listener to validate the input
        addValueChangeListener(event -> {
            String value = event.getValue();
            try {
                if (value != null && !value.isEmpty()) {
                    Short.parseShort(value);
                    setInvalid(false);
                    setErrorMessage(null);
                }
            } catch (NumberFormatException e) {
                setInvalid(true);
                setErrorMessage("El valor Ingresado es invalido");
            }
        });
        if(required){
            addBlurListener(event -> {
                if (isEmpty()) {
                    setInvalid(true);
                    setErrorMessage(label+" es requerido");
                }
            });
        }
    }
}