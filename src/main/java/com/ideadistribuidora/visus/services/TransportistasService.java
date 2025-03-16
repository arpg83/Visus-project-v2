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
import com.ideadistribuidora.visus.data.Transportistas;
import com.ideadistribuidora.visus.data.TransportistasBancos;
import com.ideadistribuidora.visus.data.TransportistasBancosId;
import com.ideadistribuidora.visus.data.Documento;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Localidades;
import com.ideadistribuidora.visus.data.enums.NivelFidelizacionEnum;
import com.ideadistribuidora.visus.data.enums.SituacionFiscalEnum;
import com.ideadistribuidora.visus.data.enums.TipoCuentaEnum;
import com.ideadistribuidora.visus.data.enums.TipoDomicilioEnum;
import com.ideadistribuidora.visus.data.repositories.BancosRepository;
import com.ideadistribuidora.visus.data.repositories.TransportistasBancosRepository;
import com.ideadistribuidora.visus.data.repositories.TransportistasRepository;
import com.ideadistribuidora.visus.data.repositories.DocumentosRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.LocalidadesRepository;

import jakarta.transaction.Transactional;

@Service
public class TransportistasService {

    private final TransportistasRepository repository;
    private final DocumentosRepository docRepository;
    private final DomiciliosRepository domiciliosRepository;
    private final LocalidadesRepository localidadesRepository;
    private final TransportistasBancosRepository transportistasBancosRepository;
    private final BancosRepository bancosRepository;

    public TransportistasService(TransportistasRepository repository, DocumentosRepository docRepository,
            DomiciliosRepository domiciliosRepository, LocalidadesRepository localidadesRepository,
            TransportistasBancosRepository transportistasBancosRepository, BancosRepository bancosRepository) {
        this.repository = repository;
        this.docRepository = docRepository;
        this.domiciliosRepository = domiciliosRepository;
        this.localidadesRepository = localidadesRepository;
        this.transportistasBancosRepository = transportistasBancosRepository;
        this.bancosRepository = bancosRepository;
    }

    public Optional<Transportistas> get(int id) {
        return repository.findById(id);
    }

    public Transportistas update(Transportistas entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Transportistas> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Transportistas> list(Pageable pageable, Specification<Transportistas> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Transportistas> transportistasList() {
        List<Transportistas> transportList = repository.findAll();
        return transportList;
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

    public NivelFidelizacionEnum[] getAllNivelFidelizacion() {
        return NivelFidelizacionEnum.values();
    }

    public SituacionFiscalEnum[] getAllSituacionFiscal() {
        return SituacionFiscalEnum.values();
    }

    public TipoDomicilioEnum[] getAllTipoDomicilio() {
        return TipoDomicilioEnum.values();
    }

    public void saveDomList(Set<Domicilios> domList) {
        domiciliosRepository.saveAll(domList);
    }

    public void deleteDomById(int id) {
        domiciliosRepository.deleteById(id);
    }

    public void deleteDom(Domicilios domicilios) {
        domiciliosRepository.delete(domicilios);
    }

    public Set<TransportistasBancos> getBancosByIdcliente(int idTransportista) {
        return repository.findBancosByIdTransportista(idTransportista);

    }

    public List<Bancos> getAllBancos() {
        return bancosRepository.findAll();
    }

    public TipoCuentaEnum[] getAllTipoCuentaEnums() {
        return TipoCuentaEnum.values();
    }

    @Transactional
    public void deleteTransportistasBancosById(TransportistasBancosId bancosId) {
        transportistasBancosRepository.deleteTransportistasBancosById(bancosId);
    }

    public void saveTransportistasBancosList(Set<TransportistasBancos> transportistasBancosList) {
        transportistasBancosRepository.saveAll(transportistasBancosList);
    }

    public Localidades findLocalidadesById(int id) {
        Optional<Localidades> loc = localidadesRepository.findById(id);
        Localidades localidades = new Localidades();
        if (loc.isPresent()) {
            localidades = loc.get();
        }
        return localidades;
    }

    public Optional<Transportistas> getTransportistaByIdDocumentoAndNumero(int idDocumento, Long numeroDocumento) {
        return repository.findByIdDocumentoAndNumeroDocumento(idDocumento, numeroDocumento);
    }

    public Domicilios saveDomicilios(Domicilios domicilios) {
        return domiciliosRepository.save(domicilios);
    }

}
