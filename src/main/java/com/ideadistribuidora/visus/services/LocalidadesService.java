package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.repositories.DepartamentosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;

@Service
public class LocalidadesService {
    LocalidadesRepository repository;
    DepartamentosRepository departamentosRepository;
    ProvinciasRepository provinciasRepository;

    public LocalidadesService(LocalidadesRepository repository, DepartamentosRepository departamentosRepository,
            ProvinciasRepository provinciasRepository) {
        this.repository = repository;
        this.departamentosRepository = departamentosRepository;
        this.provinciasRepository = provinciasRepository;
    }

    public Optional<Localidades> get(int id) {
        return repository.findById(id);
    }

    public Localidades update(Localidades entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Localidades> list(Pageable pageable) {
        Page<Localidades> locList = repository.findAll(pageable);
        locList.stream().forEach(l -> {
            l.setNombreDepartamento(l.getDepartamentos().getNombre());
        });
        return locList;
    }

    public Page<Localidades> list(Pageable pageable, Specification<Localidades> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Localidades> localList() {
        List<Localidades> locList = repository.findAll();
        locList.stream().forEach(l -> {
            l.setNombreDepartamento(l.getDepartamentos().getNombre());
            l.setProvincias(l.getDepartamentos().getProvincias());
        });
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
