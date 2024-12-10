package com.ideadistribuidora.visus.services;

import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.ideadistribuidora.visus.data.Localidades;
import com.vaadin.hilla.exception.EndpointException;

public class LocalidadesEndPoint {
    private LocalidadesService service;

    public LocalidadesEndPoint(LocalidadesService service) {
        this.service = service;
    }

    public Page<Localidades> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Localidades> get(int id) {
        return service.get(id);
    }

    public Localidades update(Localidades entity) {
        try {
            return service.update(entity);
        } catch (OptimisticLockingFailureException e) {
            throw new EndpointException("Somebody else has updated the data while you were making changes.");
        }
    }

    public void delete(int id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
