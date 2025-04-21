package com.ideadistribuidora.visus.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ideadistribuidora.visus.data.Listas;
import com.ideadistribuidora.visus.data.ListasPorcentuales;
import com.ideadistribuidora.visus.data.Porcentuales;
import com.ideadistribuidora.visus.data.repositories.ListasPorcentualesRepository;
import com.ideadistribuidora.visus.data.repositories.ListasRepository;
import com.ideadistribuidora.visus.data.repositories.PorcentualesRepository;

@Service
public class ListasPorcentualesService {

    @Autowired
    private ListasRepository listasRepository;

    @Autowired
    private PorcentualesRepository porcentualesRepository;

    @Autowired
    private ListasPorcentualesRepository listasPorcentualesRepository;

    public List<Listas> findAllListas() {
        return listasRepository.findAll();
    }

    public Optional<Listas> findById(int id) {
        return listasRepository.findById(id);
    }

    @Transactional
    public ListasPorcentuales save(ListasPorcentuales listasPorcentuales) {
        return listasPorcentualesRepository.save(listasPorcentuales);
    }

    @Transactional
    public void delete(List<ListasPorcentuales> lp) {
        listasPorcentualesRepository.deleteAll(lp);
    }

    public Optional<ListasPorcentuales> get(int id) {
        return listasPorcentualesRepository.findById(id);
    }

    public Collection<Porcentuales> findAllPorcentuales() {
        return porcentualesRepository.findAll();
    }

    public List<ListasPorcentuales> listasPorcentualesList() {
       return  listasPorcentualesRepository.findAll();
    }

    public Listas findListasById(int int1) {
         Optional<Listas> list = listasRepository.findById(int1);
        Listas listas = new Listas();
        if (list.isPresent()) {
            listas = list.get();
        }
        return listas;
    }

    public Porcentuales findPorcentualById(int int1) {
        Optional<Porcentuales> porc = porcentualesRepository.findById(int1);
        Porcentuales porcentuales = new Porcentuales();
        if (porc.isPresent()) {
            porcentuales = porc.get();
        }
        return porcentuales;
    }

}
