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
import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.ClientesBancos;
import com.ideadistribuidora.visus.data.ClientesBancosId;
import com.ideadistribuidora.visus.data.Documento;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.enums.EstadoCivilEnum;
import com.ideadistribuidora.visus.data.enums.EstadoClienteEnum;
import com.ideadistribuidora.visus.data.enums.NivelFidelizacionEnum;
import com.ideadistribuidora.visus.data.enums.SexoEnum;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesBancosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;

import jakarta.transaction.Transactional;

@Service
public class ClientesService {

    private final ClientesRepository repository;
    private final DocumentosRepository docRepository;
    private final DomiciliosRepository domiciliosRepository;
    private final LocalidadesRepository localidadesRepository;
    private final ClientesBancosRepository clientesBancosRepository;
    private final BancosRepository bancosRepository;

    public ClientesService(ClientesRepository repository, DocumentosRepository docRepository,
            DomiciliosRepository domiciliosRepository, LocalidadesRepository localidadesRepository,
            ClientesBancosRepository clientesBancosRepository, BancosRepository bancosRepository) {
        this.repository = repository;
        this.docRepository = docRepository;
        this.domiciliosRepository = domiciliosRepository;
        this.localidadesRepository = localidadesRepository;
        this.clientesBancosRepository = clientesBancosRepository;
        this.bancosRepository = bancosRepository;
    }

    public Optional<Clientes> get(int id) {
        return repository.findById(id);
    }

    public Clientes update(Clientes entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Clientes> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Clientes> list(Pageable pageable, Specification<Clientes> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Clientes> clienteList() {
        List<Clientes> cliList = repository.findAll();
        return cliList;
    }

    public List<Domicilios> domicilioList() {
        List<Domicilios> domList = domiciliosRepository.findAll();
        domList.stream().forEach(d -> {
            d.setDireccion(d.getCalle() + " " + d.getNumero());
        });
        return domList;
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

    public List<Localidades> getAllLocalidades() {
        return localidadesRepository.findAll();
    }

    public SexoEnum[] getAllSex() {
        return SexoEnum.values();
    }

    public NivelFidelizacionEnum[] getAllNivelFidelizacion() {
        return NivelFidelizacionEnum.values();
    }

    public SituacionFiscalEnum[] getAllSituacionFiscal() {
        return SituacionFiscalEnum.values();
    }

    public EstadoClienteEnum[] getAllEstadoCliente() {
        return EstadoClienteEnum.values();
    }

    public EstadoCivilEnum[] getAllEstadoCivil() {
        return EstadoCivilEnum.values();
    }

    public TipoDomicilioEnum[] getAllTipoDomicilio() {
        return TipoDomicilioEnum.values();
    }

    public void saveDomList(Set<Domicilios> domList) {
        domiciliosRepository.saveAll(domList);
    }

    public Set<Domicilios> getDomByIDCliente(int idCliente) {
        return repository.findDomiciliosByIdCliente(idCliente);
    }

    public void deleteDomById(int id) {
        domiciliosRepository.deleteById(id);
    }

    public void deleteAllDom(Set<Domicilios> domicilios) {
        domiciliosRepository.deleteAll(domicilios);
    }

    public Set<ClientesBancos> getBancosByIdcliente(int idCliente) {
        return repository.findBancosByIdCliente(idCliente);

    }

    public List<Bancos> getAllBancos() {
        return bancosRepository.findAll();
    }

    public TipoCuentaEnum[] getAllTipoCuentaEnums() {
        return TipoCuentaEnum.values();
    }

    @Transactional
    public void deleteClientesBancosById(ClientesBancosId bancosId) {
        clientesBancosRepository.deleteClientesBancosById(bancosId);
    }

    public void saveClientesBancosList(Set<ClientesBancos> clientesBancosList) {
        clientesBancosRepository.saveAll(clientesBancosList);
    }

}
