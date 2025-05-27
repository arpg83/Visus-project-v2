package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Coeficientes;
import com.ideadistribuidora.visus.data.repositories.CoeficientesRepository;

@Service
public class CoeficientesService {
    CoeficientesRepository repository;
  

    public CoeficientesService(CoeficientesRepository repository) {
        this.repository = repository;
       
    }

    public Optional<Coeficientes> get(int id) {
        return repository.findById(id);
    }

    public Coeficientes update(Coeficientes entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Coeficientes> list(Pageable pageable, Specification<Coeficientes> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Coeficientes> coeList() {
        List<Coeficientes> coeList = repository.findAll();
        return coeList;
    }

    public int count() {
        return (int) repository.count();
    }

}
