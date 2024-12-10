package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Presentaciones;
import com.ideadistribuidora.visus.data.repositories.PresentacionesRepository;

@Service
public class PresentacionesService {
    PresentacionesRepository repository;

    public PresentacionesService(PresentacionesRepository repository) {
        this.repository = repository;
    }

    public Optional<Presentaciones> get(int id) {
        return repository.findById(id);
    }

    public Presentaciones update(Presentaciones entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Presentaciones> list(Pageable pageable) {
        Page<Presentaciones> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Presentaciones> list(Pageable pageable, Specification<Presentaciones> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Presentaciones> presentacionesList() {
        List<Presentaciones> presList = repository.findAll();
        return presList;
    }

    public int count() {
        return (int) repository.count();
    }

}
