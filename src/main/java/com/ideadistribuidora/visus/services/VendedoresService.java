package com.ideadistribuidora.visus.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Comisiones;
import com.ideadistribuidora.visus.data.Documento;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Provincias;
import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.repositories.ComisionesRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.VendedoresRepository;
import com.ideadistribuidora.visus.data.repositories.ZonasRepository;

@Service
public class VendedoresService {
    private final VendedoresRepository repository;
    private final DocumentosRepository docRepository;
    private final LocalidadesRepository localidadesRepository;
    private final ZonasRepository zonasRepository;
    private final ComisionesRepository comisionesRepository;
    private final DomiciliosRepository domiciliosRepository;
    

    public VendedoresService(VendedoresRepository repository, DocumentosRepository docRepository
            , LocalidadesRepository localidadesRepository, ZonasRepository zonasRepository
            , ComisionesRepository comisionesRepository
            , DomiciliosRepository domiciliosRepository) {    
        this.repository = repository;
        this.docRepository = docRepository;
        this.localidadesRepository = localidadesRepository;
        this.zonasRepository = zonasRepository;
        this.comisionesRepository = comisionesRepository;
        this.domiciliosRepository = domiciliosRepository;
        
    }

    public Optional<Vendedores> get(int id) {
        return repository.findById(id);
    }

    public List<Vendedores> vendedoresList() {
        List<Vendedores> provList = repository.findAll();
        return provList;
    }

    public Vendedores update(Vendedores entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Vendedores> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Vendedores> list(Pageable pageable, Specification<Vendedores> filter) {
        return repository.findAll(filter, pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public Map<Integer, String> getDocumentList() {
        System.out.println("lista de documentos: " + docRepository.findAll());
        List<Documento> docs = docRepository.findAll();
        Map<Integer, String> docMap = new HashMap<>();
        docs.stream().forEach(d -> {
            docMap.put(d.getId_Documento(), d.getDescripcion());
        });
        return docMap;
    } 
   

    public TipoCuentaEnum[] getAllTipoCuentaEnums() {
        return TipoCuentaEnum.values();
    }

    public List<Localidades> getAllLocalidades() {
        return localidadesRepository.findAll();
    }

    public TipoDomicilioEnum[] getAllTipoDomicilio() {
        return TipoDomicilioEnum.values();
    }
      
    public List<Comisiones> getAllComisiones() {

        // Implement the logic to retrieve all Comisiones

        return new ArrayList<>();

    }

    public void deleteZonaById(int id) {
        zonasRepository.deleteById(id);
    }

    public void deleteComisionesById(int idComision) {
        comisionesRepository.deleteById(idComision);
    }

    public Localidades findLocalidadesById(int id) {
         Optional<Localidades> loc = localidadesRepository.findById(id);
        Localidades localidades = new Localidades();
        if (loc.isPresent()) {
            localidades = loc.get();
        }
        return localidades;
    }

    public Domicilios saveDomicilios(Domicilios domicilios) {
        return domiciliosRepository.save(domicilios);
    }

    public void saveZonaList(Set<Zonas> zonaList) {
        zonasRepository.saveAll(zonaList);
    }

    public void saveComisionesList(Set<Comisiones> comisionesList) {
        comisionesRepository.saveAll(comisionesList);
    }

    public Optional<Vendedores> getVendedoresByIdDocumentoAndNumero(int idDocumento, Long numeroDeDocumento) {
        return repository.findByIdDocumentoAndNumeroDeDocumento(idDocumento, numeroDeDocumento);
    }

    public Set<Zonas> getZonasByIdVendedores(int idVendedor) {
        return repository.findZonasByIdVendedor(idVendedor);
    }

    
   

   

}
