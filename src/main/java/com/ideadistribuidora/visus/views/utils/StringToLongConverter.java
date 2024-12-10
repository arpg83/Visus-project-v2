package com.ideadistribuidora.visus.views.utils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Result<Long> convertToModel(String value, ValueContext context) {
        try {
            if (value == null || value.isEmpty()) {
                return Result.ok(null);
            }
            if (!value.matches("\\d+")) {
                throw new NumberFormatException();
            }
            return Result.ok(Long.valueOf(value));
        } catch (NumberFormatException e) {
            Notification.show("El valor ingresado no es un número");
            return Result.error("El valor ingresado no es un número");
        }
    }

    @Override
    public String convertToPresentation(Long value, ValueContext context) {
        return value == null ? "" : value.toString();
    }

}
