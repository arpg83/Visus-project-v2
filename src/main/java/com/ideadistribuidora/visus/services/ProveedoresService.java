package com.ideadistribuidora.visus.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Bancos;
import com.ideadistribuidora.visus.data.Documento;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.ProveedoresBancos;
import com.ideadistribuidora.visus.data.ProveedoresBancosId;
import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresRepository;

import jakarta.transaction.Transactional;

@Service
public class ProveedoresService {

    private final ProveedoresRepository repository;
    private final DocumentosRepository docRepository;
    private final DomiciliosRepository domiciliosRepository;
    private final BancosRepository bancosRepository;
    private final ProveedoresBancosRepository proveedoresBancosRepository;
    private final LocalidadesRepository localidadesRepository;

    public ProveedoresService(ProveedoresRepository repository, DocumentosRepository docRepository,
            DomiciliosRepository domiciliosRepository, BancosRepository bancosRepository,
            ProveedoresBancosRepository proveedoresBancosRepository, LocalidadesRepository localidadesRepository) {
        this.repository = repository;
        this.docRepository = docRepository;
        this.domiciliosRepository = domiciliosRepository;
        this.bancosRepository = bancosRepository;
        this.proveedoresBancosRepository = proveedoresBancosRepository;
        this.localidadesRepository = localidadesRepository;
    }

    public Optional<Proveedores> get(int id) {
        return repository.findById(id);
    }

    public List<Proveedores> proveedList() {
        List<Proveedores> provList = repository.findAll();
        return provList;
    }

    public Proveedores update(Proveedores entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Proveedores> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Proveedores> list(Pageable pageable, Specification<Proveedores> filter) {
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

    public Set<Domicilios> getDomByIDProveedor(int idProveedor) {
        return repository.findDomiciliosByIdProveedor(idProveedor);
    }

    public Set<ProveedoresBancos> getBancosByIdProveedor(int idProveedor) {
        return repository.findBancosByIdProveedor(idProveedor);

    }

    public void saveDomList(Set<Domicilios> domList) {
        domiciliosRepository.saveAll(domList);
    }

    public void deleteAllDom(Set<Domicilios> domicilios) {
        domiciliosRepository.deleteAll(domicilios);
    }

    public List<Bancos> getAllBancos() {
        return bancosRepository.findAll();
    }

    public TipoCuentaEnum[] getAllTipoCuentaEnums() {
        return TipoCuentaEnum.values();
    }

    @Transactional
    public void deleteProveedoresBancosById(ProveedoresBancosId bancosId) {
        proveedoresBancosRepository.deleteProveedoresBancosById(bancosId);
    }

    public void saveProveedoresBancosList(Set<ProveedoresBancos> proveedoresBancosList) {
        proveedoresBancosRepository.saveAll(proveedoresBancosList);
    }

    public List<Localidades> getAllLocalidades() {
        return localidadesRepository.findAll();
    }

    public TipoDomicilioEnum[] getAllTipoDomicilio() {
        return TipoDomicilioEnum.values();
    }

    public void deleteDomById(int id) {
        domiciliosRepository.deleteById(id);
    }

}
