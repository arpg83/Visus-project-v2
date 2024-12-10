package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.repositories.DepositosRepository;

@Service
public class DepositosService {
    DepositosRepository repository;

    public DepositosService(DepositosRepository repository) {
        this.repository = repository;
    }

    public Optional<Depositos> get(int id) {
        return repository.findById(id);
    }

    public Depositos update(Depositos entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Depositos> list(Pageable pageable) {
        Page<Depositos> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Depositos> list(Pageable pageable, Specification<Depositos> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Depositos> depositosList() {
        List<Depositos> depList = repository.findAll();
        return depList;
    }

    public int count() {
        return (int) repository.count();
    }

}
