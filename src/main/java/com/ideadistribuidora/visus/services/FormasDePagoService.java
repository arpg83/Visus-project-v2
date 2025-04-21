package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Coeficientes;
import com.ideadistribuidora.visus.data.FormasDePago;
import com.ideadistribuidora.visus.data.repositories.CoeficientesRepository;
import com.ideadistribuidora.visus.data.repositories.FormasDePagoRepository;

@Service
public class FormasDePagoService {
    FormasDePagoRepository repository;
    CoeficientesRepository coeficientesRepository;

    public FormasDePagoService(FormasDePagoRepository repository, CoeficientesRepository coeficientesRepository) {
        this.repository = repository;
        this.coeficientesRepository = coeficientesRepository;
    }

    public Optional<FormasDePago> get(int id) {
        return repository.findById(id);
    }

    public FormasDePago update(FormasDePago entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<FormasDePago> list(Pageable pageable) {
        Page<FormasDePago> depList = repository.findAll(pageable);
        return depList;
    }

    public Page<FormasDePago> list(Pageable pageable, Specification<FormasDePago> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<FormasDePago> formasDePagoList() {
        List<FormasDePago> fpList = repository.findAll();
        return fpList;
    }

    public int count() {
        return (int) repository.count();
    }

    public Coeficientes findCoeficientesById(int idCoeficiente) {
         Optional<Coeficientes> coe = coeficientesRepository.findById(idCoeficiente);
        Coeficientes coeficientes = new Coeficientes();
        if (coe.isPresent()) {
            coeficientes = coe.get();
        }
        return coeficientes;
        
    }

    public List<Coeficientes> getAllCoefiencientes() {
       return coeficientesRepository.findAll();
    }

  
}
