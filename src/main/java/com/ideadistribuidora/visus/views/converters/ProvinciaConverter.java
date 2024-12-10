package com.ideadistribuidora.visus.views.converters;

import java.util.List;
import java.util.Optional;

import com.ideadistribuidora.visus.data.Provincias;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

public class ProvinciaConverter implements Converter<String, Provincias> {
    private final List<Provincias> provincias;

    public ProvinciaConverter(List<Provincias> provincias) {
        this.provincias = provincias;
    }

    @Override
    public Result<Provincias> convertToModel(String value, ValueContext context) {
        Optional<Provincias> provincia = provincias.stream()
                .filter(p -> p.getProvincia().equals(value))
                .findFirst();
        return provincia.map(Result::ok)
                .orElseGet(() -> Result.error("Provincia no encontrada"));
    }

    @Override
    public String convertToPresentation(Provincias provincia, ValueContext context) {
        return provincia == null ? "" : provincia.getProvincia();
    }
}