package com.ideadistribuidora.visus.services;

import com.ideadistribuidora.visus.data.Clientes;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.Endpoint;
import com.vaadin.hilla.exception.EndpointException;
import java.util.Optional;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Endpoint
@AnonymousAllowed
public class ClientesEndpoint {

    private final ClientesService service;

    public ClientesEndpoint(ClientesService service) {
        this.service = service;
    }

    public Page<Clientes> list(Pageable page) {
        return service.list(page);
    }

    public Optional<Clientes> get(Long id) {
        return service.get(id);
    }

    public Clientes update(Clientes entity) {
        try {
            return service.update(entity);
        } catch (OptimisticLockingFailureException e) {
            throw new EndpointException("Somebody else has updated the data while you were making changes.");
        }
    }

    public void delete(Long id) {
        service.delete(id);
    }

    public int count() {
        return service.count();
    }

}
