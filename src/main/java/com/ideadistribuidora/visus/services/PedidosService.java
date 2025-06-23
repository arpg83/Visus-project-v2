package com.ideadistribuidora.visus.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.ListasPorcentuales;
import com.ideadistribuidora.visus.data.Medidas;
import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosItems;
import com.ideadistribuidora.visus.data.PedidosListas;
import com.ideadistribuidora.visus.data.Transportistas;
import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.Zonas;
import com.ideadistribuidora.visus.data.repositories.ArticulosRepository;
import com.ideadistribuidora.visus.data.repositories.ClientesRepository;
import com.ideadistribuidora.visus.data.repositories.DomiciliosRepository;
import com.ideadistribuidora.visus.data.repositories.ListasPorcentualesRepository;
import com.ideadistribuidora.visus.data.repositories.ListasRepository;
import com.ideadistribuidora.visus.data.repositories.MedidasRepository;
import com.ideadistribuidora.visus.data.repositories.PedidosItemsRepository;
import com.ideadistribuidora.visus.data.repositories.PedidosListasRepository;
import com.ideadistribuidora.visus.data.repositories.PedidosRepository;
import com.ideadistribuidora.visus.data.repositories.TransportistasRepository;
import com.ideadistribuidora.visus.data.repositories.VendedoresRepository;
import com.ideadistribuidora.visus.data.repositories.ZonasRepository;

@Service
public class PedidosService {

    PedidosRepository repository;
    ClientesRepository clientesRepository;
    VendedoresRepository vendedoresRepository;
    ZonasRepository zonasRepository;
    ListasRepository listasRepository;
    ArticulosRepository articulosRepository;
    MedidasRepository medidasRepository;
    TransportistasRepository transportistasRepository;
    DomiciliosRepository domiciliosRepository;
    ListasPorcentualesRepository listasPorcentualesRepository;
    PedidosItemsRepository pedidosItemsRepository;
    PedidosListasRepository pedidosListasRepository;

    public PedidosService(PedidosRepository repository, ClientesRepository clientesRepository,
            VendedoresRepository vendedoresRepository, ZonasRepository zonasRepository,
            ListasRepository listasRepository, ArticulosRepository articulosRepository,
            MedidasRepository medidasRepository, TransportistasRepository transportistasRepository,
            DomiciliosRepository domiciliosRepository, ListasPorcentualesRepository listasPorcentualesRepository,
            PedidosItemsRepository pedidosItemsRepository,PedidosListasRepository pedidosListasRepository) {
        this.repository = repository;
        this.clientesRepository = clientesRepository;
        this.vendedoresRepository = vendedoresRepository;
        this.zonasRepository = zonasRepository;
        this.listasRepository = listasRepository;
        this.articulosRepository = articulosRepository;
        this.medidasRepository = medidasRepository;
        this.transportistasRepository = transportistasRepository;
        this.domiciliosRepository = domiciliosRepository;
        this.listasPorcentualesRepository = listasPorcentualesRepository;
        this.pedidosItemsRepository = pedidosItemsRepository;
        this.pedidosListasRepository = pedidosListasRepository;
    }

    public Optional<Pedidos> get(int id) {
        return repository.findById(id);
    }

    public Pedidos update(Pedidos entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Pedidos> list(Pageable pageable) {
        Page<Pedidos> pedList = repository.findAll(pageable);
        return pedList;
    }

    public Page<Pedidos> list(Pageable pageable, Specification<Pedidos> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Pedidos> pedidosList() {
        List<Pedidos> pedList = repository.findAll();
        return pedList;
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Clientes> getAllClientes() {
        return clientesRepository.findAll();
    }

    public Clientes findById(int id) {
        Optional<Clientes> cli = clientesRepository.findById(id);
        Clientes clientes = new Clientes();
        if (cli.isPresent()) {
            clientes = cli.get();
        }
        return clientes;
    }

    public List<Clientes> getAllclientes() {
        return clientesRepository.findAll();
    }

    public List<Vendedores> getAllVendedores() {
        return vendedoresRepository.findAll();
    }

    public List<Zonas> getAllZonas() {
        return zonasRepository.findAll();
    }

    public List<ListasPorcentuales> getAllListasPorc() {
        return listasPorcentualesRepository.findAll();
    }

    public List<Articulos> getAllArticulos() {
        return articulosRepository.findAll();
    }

    public List<Medidas> getAllMedidas() {
        return medidasRepository.findAll();
    }

    public List<Domicilios> getDomicilioByIdCliente(int idCliente) {
        return new ArrayList<>(clientesRepository.findDomiciliosByIdCliente(idCliente));
    }

    public List<Transportistas> getAllTransportistas() {
        return transportistasRepository.findAll();
    }

    public List<Zonas> getZonasByIdVendedor(int idVendedor) {
       return new ArrayList<>(vendedoresRepository.findZonasByIdVendedor(idVendedor));
    }

    public Articulos findArticulosById(int int1) {
        Optional<Articulos> art = articulosRepository.findById(int1);
        Articulos articulos = new Articulos();
        if (art.isPresent()) {
            articulos = art.get();
        }
        return articulos;
    }

    public Object findByIdClientes(int int1) {
        Optional<Clientes> cli = clientesRepository.findById(int1);
        Clientes clientes = new Clientes();
        if (cli.isPresent()) {
            clientes = cli.get();
        }
        return clientes;
    }

    public Vendedores findByIdVendedores(int int1) {
        Optional<Vendedores> ven = vendedoresRepository.findById(int1);
        Vendedores vendedores = new Vendedores();
        if (ven.isPresent()) {
            vendedores = ven.get();
        }
        return vendedores;
    }

    public Zonas findByIdZonas(int int1) {
        Optional<Zonas> zon = zonasRepository.findById(int1);
        Zonas zonas = new Zonas();
        if (zon.isPresent()) {
            zonas = zon.get();
        }
        return zonas;
    }

    public Domicilios findByIdDomicilios(int int1) {
        Optional<Domicilios> dom = domiciliosRepository.findById(int1);
        Domicilios domicilios = new Domicilios();
        if (dom.isPresent()) {
            domicilios = dom.get();
        }
        return domicilios;
    }

    public Integer getNextIdPedido() {
        Integer nextId = repository.getNextIdPedido().orElse(1);
        return nextId + 1;
    }

    public Articulos updateArticulos(Articulos idArticulo) {
        Articulos artic = articulosRepository.save(idArticulo);
        return artic;
    }

    public void saveAllPedItemsList(List<PedidosItems> pedidosItemsList) {
        pedidosItemsRepository.saveAll(pedidosItemsList);
    }

    public void savePedidosListas(PedidosListas pedidosListas) {
       pedidosListasRepository.save(pedidosListas);
    }

    public List<PedidosItems> findPedidosItemsByIdPedidos(Pedidos pedidos) {
        return pedidosItemsRepository.findPedidosItemsByIdPedido(pedidos);
    }

    public List<PedidosListas> findPedidosListasByIdPedido(Pedidos pedidos) {
        return pedidosListasRepository.findPedidosListasByIdPedido(pedidos);
    }

}
