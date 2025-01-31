package com.ideadistribuidora.visus.views.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToShortConverter implements Converter<String, Short> {

    @Override
    public Result<Short> convertToModel(String value, ValueContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return Result.ok(null);
            }
            // if (!value.matches("\\d+")) {
            //     throw new NumberFormatException();
            // }
            return Result.ok(Short.valueOf(value));
        } catch (NumberFormatException e) {
            Notification.show("El valor ingresado es invalido");
            return Result.error("El valor ingresado es invalido");
        }
    }

    @Override
    public String convertToPresentation(Short value, ValueContext context) {
        return value == null ? "" : value.toString();
    }

}
