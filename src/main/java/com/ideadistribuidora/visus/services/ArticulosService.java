package com.ideadistribuidora.visus.services;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.ideadistribuidora.visus.data.Alicuotas;
import com.ideadistribuidora.visus.data.Articulos;
import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.Lineas;
import com.ideadistribuidora.visus.data.Medidas;
import com.ideadistribuidora.visus.data.Presentaciones;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.Rubros;
import com.ideadistribuidora.visus.data.Ubicaciones;
import com.ideadistribuidora.visus.data.enums.EstadoArticuloEnum;
import com.ideadistribuidora.visus.data.enums.TipoArticuloEnum;
import com.ideadistribuidora.visus.data.repositories.AlicuotasRepository;
import com.ideadistribuidora.visus.data.repositories.ArticulosRepository;
import com.ideadistribuidora.visus.data.repositories.DepositosRepository;
import com.ideadistribuidora.visus.data.repositories.LineasRepository;
import com.ideadistribuidora.visus.data.repositories.MedidasRepository;
import com.ideadistribuidora.visus.data.repositories.PresentacionesRepository;
import com.ideadistribuidora.visus.data.repositories.ProveedoresRepository;
import com.ideadistribuidora.visus.data.repositories.RubrosRepository;
import com.ideadistribuidora.visus.data.repositories.UbicacionesRepository;

@Service
public class ArticulosService {

    private final ArticulosRepository repository;
    private final RubrosRepository rubrosRepository;
    private final LineasRepository lineasRepository;
    private final MedidasRepository medidasRepository;
    private final PresentacionesRepository presentacionesRepository;
    private final DepositosRepository depositosRepository;
    private final UbicacionesRepository ubicacionesRepository;
    private final ProveedoresRepository proveedoresRepository;
    private final AlicuotasRepository alicuotasRepository;

    public ArticulosService(ArticulosRepository repository, RubrosRepository rubrosRepository,
            LineasRepository lineasRepository, MedidasRepository medidasRepository,
            PresentacionesRepository presentacionesRepository, DepositosRepository depositosRepository,
            UbicacionesRepository ubicacionesRepository, ProveedoresRepository proveedoresRepository,
            AlicuotasRepository alicuotasRepository) {
        this.repository = repository;
        this.rubrosRepository = rubrosRepository;
        this.lineasRepository = lineasRepository;
        this.medidasRepository = medidasRepository;
        this.presentacionesRepository = presentacionesRepository;
        this.depositosRepository = depositosRepository;
        this.ubicacionesRepository = ubicacionesRepository;
        this.proveedoresRepository = proveedoresRepository;
        this.alicuotasRepository = alicuotasRepository;
    }

    public Optional<Articulos> get(int id) {
        // return repository.findById(id);
        return repository.finByIdWithGanancia(id);
    }

    public Articulos update(Articulos entity) {
        return repository.save(entity);
    }

    public void delete(int id) {
        repository.deleteById(id);
    }

    public Page<Articulos> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<Articulos> list(Pageable pageable, Specification<Articulos> filter) {
        return repository.findAll(filter, pageable);
    }

    public List<Articulos> articulosList() {
        List<Articulos> artList = repository.findAllWithGanancia();

        return artList;
        // .stream()
        // .map(articulo -> {
        // BigDecimal ganancia = articulo.getPrecioCosto()
        // .multiply(articulo.getMargenUtilidad())
        // .divide(BigDecimal.valueOf(100));
        // articulo.setGanancia(ganancia); // Asignar el resultado a la variable
        // 'ganancia'
        // articulo.setPrecioFinalSinIva(articulo.getPrecioCosto().add(articulo.getGanancia()));
        // BigDecimal alic = articulo.getAlicuota().getDescripcion().contains("%")
        // ? BigDecimal.valueOf(Double
        // .valueOf(articulo.getAlicuota().getDescripcion()
        // .replace("%", "")))
        // : BigDecimal.ZERO;
        // BigDecimal resConIva = articulo.getPrecioFinalSinIva().multiply(alic)
        // .divide(BigDecimal.valueOf(100));
        // articulo.setPrecioFinalConIva(articulo.getPrecioFinalSinIva().add(resConIva));
        // return articulo;
        // })
        // .collect(Collectors.toList());
    }

    public int count() {
        return (int) repository.count();
    }

    public List<Rubros> getAllRubros() {
        List<Rubros> rubrosList = rubrosRepository.findAll();
        return rubrosList;
    }

    public List<Lineas> getAllLineas() {
        List<Lineas> lineasList = lineasRepository.findAll();
        return lineasList;
    }

    public List<Medidas> getAllMedidas() {
        List<Medidas> medidasList = medidasRepository.findAll();
        return medidasList;
    }

    public List<Presentaciones> getAllPresentaciones() {
        List<Presentaciones> presentacionesList = presentacionesRepository.findAll();
        return presentacionesList;
    }

    public List<Depositos> getAllDepositos() {
        List<Depositos> depositosList = depositosRepository.findAll();
        return depositosList;
    }

    public List<Ubicaciones> getAllUbicacion() {
        List<Ubicaciones> ubicacionesList = ubicacionesRepository.findAll();
        return ubicacionesList;
    }

    public List<Proveedores> getAllProveedores() {
        List<Proveedores> proveedoresList = proveedoresRepository.findAll();
        return proveedoresList;
    }

    public TipoArticuloEnum[] getAllTipoArticulo() {
        return TipoArticuloEnum.values();
    }

    public EstadoArticuloEnum[] getAllEstados() {
        return EstadoArticuloEnum.values();
    }

    public Rubros findByIdRubros(int idRubros) {
        Optional<Rubros> rub = rubrosRepository.findById(idRubros);
        Rubros rubros = new Rubros();
        if (rub.isPresent()) {
            rubros = rub.get();
        }
        return rubros;

    }

    public Lineas findByIdLinea(int idLineas) {
        Optional<Lineas> lin = lineasRepository.findById(idLineas);
        Lineas lineas = new Lineas();
        if (lin.isPresent()) {
            lineas = lin.get();
        }
        return lineas;
    }

    public Proveedores findByIdProveedores(int idProveedores) {
        Optional<Proveedores> prov = proveedoresRepository.findById(idProveedores);
        Proveedores proveedores = new Proveedores();
        if (prov.isPresent()) {
            proveedores = prov.get();
        }
        return proveedores;
    }

    public Ubicaciones findByIdUbicaciones(int idUbicaciones) {
        Optional<Ubicaciones> ub = ubicacionesRepository.findById(idUbicaciones);
        Ubicaciones ubicaciones = new Ubicaciones();
        if (ub.isPresent()) {
            ubicaciones = ub.get();
        }
        return ubicaciones;
    }

    public Presentaciones findByIdPresentaciones(int idPresentaciones) {
        Optional<Presentaciones> pres = presentacionesRepository.findById(idPresentaciones);
        Presentaciones presentaciones = new Presentaciones();
        if (pres.isPresent()) {
            presentaciones = pres.get();
        }
        return presentaciones;
    }

    public Depositos findByIdDepositos(int idDepositos) {
        Optional<Depositos> dep = depositosRepository.findById(idDepositos);
        Depositos depositos = new Depositos();
        if (dep.isPresent()) {
            depositos = dep.get();
        }
        return depositos;
    }

    public Medidas findByIdMedidas(int idMedidas) {
        Optional<Medidas> med = medidasRepository.findById(idMedidas);
        Medidas medidas = new Medidas();
        if (med.isPresent()) {
            medidas = med.get();
        }
        return medidas;
    }

    public List<Alicuotas> getAllAlicuotas() {
        return alicuotasRepository.findAll();
    }

    public Alicuotas findByIdAlicuotas(int idAlicuotas) {
        Optional<Alicuotas> alicuotas = alicuotasRepository.findById(idAlicuotas);
        Alicuotas alicuotas1 = new Alicuotas();
        if (alicuotas.isPresent()) {
            alicuotas1 = alicuotas.get();
        }
        return alicuotas1;
    }

    public List<Ubicaciones> findUbicacionByDeposito(Depositos selectedDepositos) {
        return ubicacionesRepository.findUbicacionesByDepositos(selectedDepositos);
    }

    public List<Lineas> findLineasByRubros(Rubros selectedRubros) {
        return lineasRepository.findLineasByRubros(selectedRubros);
    }
}
