package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Medidas;
import com.ideadistribuidora.visus.data.repositories.MedidasRepository;

@Service
public class MedidasService {
    MedidasRepository repository;

    public MedidasService(MedidasRepository repository) {
        this.repository = repository;
    }

    public Optional<Medidas> get(int id) {
        return repository.findById(id);
    }

    public Medidas update(Medidas entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Medidas> list(Pageable pageable) {
        Page<Medidas> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Medidas> list(Pageable pageable, Specification<Medidas> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Medidas> medidasList() {
        List<Medidas> medList = repository.findAll();
        return medList;
    }

    public int count() {
        return (int) repository.count();
    }
}
