package com.ideadistribuidora.visus.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Pagos;
import com.ideadistribuidora.visus.data.repositories.PagosRepository;
@Service
public class PagosService {

    private final PagosRepository repository;

    public PagosService(PagosRepository repository) {
        this.repository = repository;
    }

    public List<Pagos> pagoList() {
        
        List<Pagos> pagosList = repository.findAll();

        return pagosList;
    }

    public void save(Pagos pagos) {
        repository.save(pagos);
    }

}
