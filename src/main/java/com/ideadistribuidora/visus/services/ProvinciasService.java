package com.ideadistribuidora.visus.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.repositories.ProvinciasRepository;

@Service
public class ProvinciasService {
    ProvinciasRepository repository;

    public ProvinciasService(ProvinciasRepository repository) {
        this.repository = repository;
    }

    public List<Provincias> getAllProvincias() {
        return repository.findAll();
    }

}
