package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.repositories.DepartamentosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;

import jakarta.transaction.Transactional;

@Service
public class DepartamentosService {
    DepartamentosRepository repository;
    ProvinciasRepository provinciasRepository;
    LocalidadesRepository localidadesRepository;

    public DepartamentosService(DepartamentosRepository repository, ProvinciasRepository provinciasRepository,
            LocalidadesRepository localidadesRepository) {
        this.repository = repository;
        this.provinciasRepository = provinciasRepository;
        this.localidadesRepository = localidadesRepository;
    }

    public Optional<Departamentos> get(int id) {
        return repository.findById(id);
    }

    public Departamentos update(Departamentos entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Departamentos> list(Pageable pageable) {
        Page<Departamentos> depList = repository.findAll(pageable);
        depList.stream().forEach(dl -> {
            dl.setNombreProvincia(dl.getProvincias().getProvincia());
        });
        return depList;
    }

    public Page<Departamentos> list(Pageable pageable, Specification<Departamentos> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Departamentos> departList() {
        List<Departamentos> depList = repository.findAll();
        depList.stream().forEach(dl -> {
            dl.setNombreProvincia(dl.getProvincias().getProvincia());
        });
        return depList;
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Provincias> getAllProvincias() {
        return provinciasRepository.findAll();
    }

    public Provincias findById(int id) {
        Optional<Provincias> prov = provinciasRepository.findById(id);
        Provincias provincias = new Provincias();
        if (prov.isPresent()) {
            provincias = prov.get();
        }
        return provincias;

    }

    @Transactional
    public void deleteLocalidadesByDepartamentos(Departamentos departamento) {
        localidadesRepository.deleteLocalidadByDepartamentos(departamento);
    }

}
