package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Rubros;
import com.ideadistribuidora.visus.data.repositories.RubrosRepository;

@Service
public class RubrosService {
    RubrosRepository repository;

    public RubrosService(RubrosRepository repository) {
        this.repository = repository;
    }

    public Optional<Rubros> get(int id) {
        return repository.findById(id);
    }

    public Rubros update(Rubros entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Rubros> list(Pageable pageable) {
        Page<Rubros> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Rubros> list(Pageable pageable, Specification<Rubros> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Rubros> rubrosList() {
        List<Rubros> rubList = repository.findAll();
        return rubList;
    }

    public int count() {
        return (int) repository.count();
    }

}
