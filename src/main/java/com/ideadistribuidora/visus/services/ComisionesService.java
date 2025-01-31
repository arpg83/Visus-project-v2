package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Comisiones;
import com.ideadistribuidora.visus.data.repositories.ComisionesRepository;
@Service
public class ComisionesService {
     ComisionesRepository repository;

    public ComisionesService(ComisionesRepository repository) {
        this.repository = repository;
    }

    public Optional<Comisiones> get(int id) {
        return repository.findById(id);
    }

    public Comisiones update(Comisiones entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Comisiones> list(Pageable pageable) {
        Page<Comisiones> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Comisiones> list(Pageable pageable, Specification<Comisiones> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Comisiones> comisionesList() {
        List<Comisiones> comisionesList = repository.findAll();
        return comisionesList;
    }

    public int count() {
        return (int) repository.count();
    }

}
