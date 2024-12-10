package com.ideadistribuidora.visus.services;

import java.util.Optional;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ideadistribuidora.visus.data.Departamentos;
import com.vaadin.hilla.exception.EndpointException;

public class DepartamentosEndPoint {

    private DepartamentosService service;

    public DepartamentosEndPoint(DepartamentosService service) {
        this.service = service;
    }

    public Page<Departamentos> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Departamentos> get(int id) {
        return service.get(id);
    }

    public Departamentos update(Departamentos entity) {
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
