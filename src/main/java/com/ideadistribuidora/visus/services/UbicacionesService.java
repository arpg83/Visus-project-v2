package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.Ubicaciones;
import com.ideadistribuidora.visus.data.repositories.DepositosRepository;
import com.ideadistribuidora.visus.data.repositories.UbicacionesRepository;

@Service
public class UbicacionesService {
    UbicacionesRepository repository;
    DepositosRepository depositosRepository;

    public UbicacionesService(UbicacionesRepository repository, DepositosRepository depositosRepository) {
        this.repository = repository;
        this.depositosRepository = depositosRepository;
    }

    public Optional<Ubicaciones> get(int id) {
        return repository.findById(id);
    }

    public Ubicaciones update(Ubicaciones entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Ubicaciones> list(Pageable pageable) {
        Page<Ubicaciones> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Ubicaciones> list(Pageable pageable, Specification<Ubicaciones> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Ubicaciones> ubicacionesList() {
        List<Ubicaciones> ubiList = repository.findAll();
        return ubiList;
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Depositos> getAllDepositos() {
        return depositosRepository.findAll();
    }

    public Depositos findById(int id) {
        Optional<Depositos> dep = depositosRepository.findById(id);
        Depositos depositos = new Depositos();
        if (dep.isPresent()) {
            depositos = dep.get();
        }
        return depositos;

    }

}
