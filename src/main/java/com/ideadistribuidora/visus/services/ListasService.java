package com.ideadistribuidora.visus.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideadistribuidora.visus.data.Listas;
import com.ideadistribuidora.visus.data.repositories.ListasRepository;
import com.ideadistribuidora.visus.data.repositories.PorcentualesRepository;

@Service
public class ListasService {

    @Autowired
    private ListasRepository listasRepository;

    @Autowired
    private PorcentualesRepository porcentualesRepository;

    public List<Listas> findAll() {
        return listasRepository.findAll();
    }

    public Optional<Listas> findById(int id) {
        return listasRepository.findById(id);
    }

    @Transactional
    public Listas save(Listas listas) {
        return listasRepository.save(listas);
    }

    @Transactional
    public void delete(int id) {
        listasRepository.deleteById(id);
    }

    public List<Listas> listasList() {
        List<Listas> listList =listasRepository.findAll();
        return listList;
    }

    public Optional<Listas> get(int id) {
        return listasRepository.findById(id);
    }
}
