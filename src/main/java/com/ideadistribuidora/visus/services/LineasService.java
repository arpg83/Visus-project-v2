package com.ideadistribuidora.visus.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.Lineas;
import com.ideadistribuidora.visus.data.Rubros;
import com.ideadistribuidora.visus.data.repositories.LineasRepository;
import com.ideadistribuidora.visus.data.repositories.RubrosRepository;

@Service
public class LineasService {
    LineasRepository repository;
    RubrosRepository rubrosRepository;

    public LineasService(LineasRepository repository, RubrosRepository rubrosRepository) {
        this.repository = repository;
        this.rubrosRepository = rubrosRepository;
    }

    public Optional<Lineas> get(int id) {
        return repository.findById(id);
    }

    public Lineas update(Lineas entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Lineas> list(Pageable pageable) {
        Page<Lineas> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Lineas> list(Pageable pageable, Specification<Lineas> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Lineas> lineasList() {
        List<Lineas> linList = repository.findAll();
        return linList;
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Rubros> getAllRubros() {
        return rubrosRepository.findAll();
    }

    public Rubros findById(int id) {
        Optional<Rubros> rub = rubrosRepository.findById(id);
        Rubros rubros = new Rubros();
        if (rub.isPresent()) {
            rubros = rub.get();
        }
        return rubros;
    }
}
