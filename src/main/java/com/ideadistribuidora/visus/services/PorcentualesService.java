package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Porcentuales;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.repositories.DepartamentosRepository;
import com.ideadistribuidora.visus.data.repositories.PorcentualesRepository;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;

@Service
public class PorcentualesService {
    PorcentualesRepository repository;
    DepartamentosRepository departamentosRepository;
    ProvinciasRepository provinciasRepository;

    public PorcentualesService(PorcentualesRepository repository, DepartamentosRepository departamentosRepository,
            ProvinciasRepository provinciasRepository) {
        this.repository = repository;
        this.departamentosRepository = departamentosRepository;
        this.provinciasRepository = provinciasRepository;
    }

    public Optional<Porcentuales> get(int id) {
        return repository.findById(id);
    }

    public Porcentuales update(Porcentuales entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Porcentuales> list(Pageable pageable) {
        Page<Porcentuales> locList = repository.findAll(pageable);
        return locList;
    }

    public Page<Porcentuales> list(Pageable pageable, Specification<Porcentuales> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Porcentuales> localList() {
        List<Porcentuales> locList = repository.findAll();
        return locList;
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Departamentos> getAllDepartamentos() {
        return departamentosRepository.findAll();
    }

    public Departamentos findById(int id) {
        Optional<Departamentos> dep = departamentosRepository.findById(id);
        Departamentos departamentos = new Departamentos();
        if (dep.isPresent()) {
            departamentos = dep.get();
        }
        return departamentos;

    }

    public List<Departamentos> findDptoByProvincias(Provincias provincia) {
        return departamentosRepository.findDptoByProvincias(provincia);
    }

    public List<Provincias> getAllProvincias() {
        return provinciasRepository.findAll();
    }

    public Provincias findProvinciaById(int id) {
        Optional<Provincias> prov = provinciasRepository.findById(id);
        Provincias provincias = new Provincias();
        if (prov.isPresent()) {
            provincias = prov.get();
        }
        return provincias;
    }

}
