package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Alicuotas;
import com.ideadistribuidora.visus.data.repositories.AlicuotasRepository;

@Service
public class AlicuotasService {
    AlicuotasRepository repository;

    public AlicuotasService(AlicuotasRepository repository) {
        this.repository = repository;
    }

    public Optional<Alicuotas> get(int id) {
        return repository.findById(id);
    }

    public Alicuotas update(Alicuotas entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Alicuotas> list(Pageable pageable) {
        Page<Alicuotas> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<Alicuotas> list(Pageable pageable, Specification<Alicuotas> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Alicuotas> alicuotasList() {
        List<Alicuotas> aliList = repository.findAll();
        return aliList;
    }

    public int count() {
        return (int) repository.count();
    }
}
