package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Bancos;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;

@Service
public class BancosService {
    BancosRepository repository;

    public BancosService(BancosRepository repository) {
        this.repository = repository;
    }

    public Optional<Bancos> get(int id) {
        return repository.findById(id);
    }

    public Bancos update(Bancos entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Bancos> list(Pageable pageable) {
        Page<Bancos> depList = repository.findAll(pageable);
        // depList.stream().forEach(dl -> {
        // dl.setNombreProvincia(dl.getProvincias().getProvincia());
        // });
        return depList;
    }

    public Page<Bancos> list(Pageable pageable, Specification<Bancos> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Bancos> bancoList() {
        List<Bancos> depList = repository.findAll();
        // depList.stream().forEach(dl -> {
        // dl.setNombreProvincia(dl.getProvincias().getProvincia());
        // });
        return depList;
    }

    public int count() {
        return (int) repository.count();
    }

}
