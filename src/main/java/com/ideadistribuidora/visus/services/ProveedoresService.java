package com.ideadistribuidora.visus.services;

import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.ProveedoresRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProveedoresService {

    private final ProveedoresRepository repository;

    public ProveedoresService(ProveedoresRepository repository) {
        this.repository = repository;
    }

    public Optional<Proveedores> get(Long id) {
        return repository.findById(id);
    }

    public Proveedores update(Proveedores entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<Proveedores> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Proveedores> list(Pageable pageable, Specification<Proveedores> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

}
