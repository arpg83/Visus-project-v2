package com.ideadistribuidora.visus.views.utils;

import java.util.stream.Stream;

import com.ideadistribuidora.visus.data.enums.EstadoClienteEnum;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class StringToEnumConverter implements Converter<String, EstadoClienteEnum> {

    @Override
    public Result<EstadoClienteEnum> convertToModel(String value, ValueContext context) {
        if (value == null) {
            return null;
        }

        return Result.ok(Stream.of(EstadoClienteEnum.values())
                .filter(c -> c.getDisplayEstadoCliente().equals(value))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new));
    }

    @Override
    public String convertToPresentation(EstadoClienteEnum value, ValueContext context) {
        if (value == null) {
            return null;
        }
        return value.getDisplayEstadoCliente();
    }

}
