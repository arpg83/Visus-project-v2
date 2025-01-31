package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.repositories.ZonasRepository;
@Service
public class ZonasService {
     ZonasRepository repository;

    public ZonasService(ZonasRepository repository) {
        this.repository = repository;
    }

    public Optional<Zonas> get(int id) {
        return repository.findById(id);
    }

    public Zonas update(Zonas entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Zonas> list(Pageable pageable) {
        Page<Zonas> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Zonas> list(Pageable pageable, Specification<Zonas> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Zonas> zonasList() {
        List<Zonas> zonasList = repository.findAll();
        return zonasList;
    }

    public int count() {
        return (int) repository.count();
    }

}
