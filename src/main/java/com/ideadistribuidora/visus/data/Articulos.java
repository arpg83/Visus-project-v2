package com.ideadistribuidora.visus.data;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.ColumnTransformer;

import com.ideadistribuidora.visus.data.enums.EstadoArticuloEnum;
import com.ideadistribuidora.visus.data.enums.TipoArticuloEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "articulos")
public class Articulos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idarticulo")
    private int idArticulo;
    @NotNull
    @Column(name = "codigo_interno")
    private String codigo_interno;
    @NotNull
    @Column(name = "codigo_barra")
    private String codigo_barra;
    @NotNull
    @Column(name = "descripcion")
    private String descripcion;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "tipo:: text", write = "?::tarticulo")
    private TipoArticuloEnum tipo;
    @NotNull
    @Column(name = "stock")
    private int stock;
    @NotNull
    @Column(name = "stock_minimo")
    private int stock_minimo;
    @NotNull
    @Column(name = "stock_maximo")
    private int stock_maximo;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idlinea", nullable = false)
    private Lineas idLinea;
    @Transient
    private Rubros rubros;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idmedida", nullable = false)
    private Medidas idMedida;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idpresentacion", nullable = false)
    private Presentaciones idPresentacion;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idproveedor", nullable = false)
    private Proveedores idProveedor;
    @NotNull
    @Column(name = "fecha_compra")
    private LocalDate fecha_compra;
    @Column(name = "fecha_vencimiento")
    private LocalDate fecha_vencimiento;
    @Column(name = "fecha_baja")
    private LocalDate fecha_baja;
    @NotNull
    @Column(name = "fecha_actprecios")
    private LocalDate fecha_actPrecios;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idubicacion", nullable = false)
    private Ubicaciones idUbicacion;
    @Transient
    private Depositos deposito;
    @NotNull
    @Column(name = "fila")
    private int fila;
    @NotNull
    @Column(name = "columna")
    private int columna;
    @NotNull
    @Column(name = "precio_costo")
    private BigDecimal precio_costo;
    @NotNull
    @Column(name = "margen_utilidad")
    private BigDecimal margen_utilidad;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "idalicuota", nullable = false)
    private Alicuotas idAlicuota;
    @Column(name = "es_bonificado")
    private boolean es_bonificado;
    @Column(name = "bonificacion")
    private BigDecimal bonificacion;
    @Enumerated(EnumType.STRING)
    @NotNull
    @ColumnTransformer(read = "estado:: text", write = "?::einventario")
    private EstadoArticuloEnum estado;
    @Column(name = "nrolote")
    private String nroLote;
    @Transient
    private BigDecimal ganancia;
    @Transient
    private BigDecimal precioFinalSinIva;
    @Transient
    private BigDecimal precioFinalConIva;

    public Articulos() {
    }

    public Articulos(int idArticulo, String codigo_interno, String codigo_barra,
            String descripcion, TipoArticuloEnum tipo, int stock, int stock_minimo,
            int stock_maximo, Lineas idLinea, Medidas idMedida,
            Presentaciones idPresentacion, Proveedores idProveedor, LocalDate fecha_compra,
            LocalDate fecha_vencimiento, LocalDate fecha_baja, LocalDate fecha_actPrecios,
            Ubicaciones idUbicacion, int fila, int columna,
            BigDecimal precio_costo, BigDecimal margen_utilidad, Alicuotas idAlicuota,
            boolean es_bonificado, BigDecimal bonificacion, EstadoArticuloEnum estado, String nroLote,
            BigDecimal ganancia, BigDecimal precioFinalSinIva) {
        this.idArticulo = idArticulo;
        this.codigo_interno = codigo_interno;
        this.codigo_barra = codigo_barra;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.stock = stock;
        this.stock_minimo = stock_minimo;
        this.stock_maximo = stock_maximo;
        this.idLinea = idLinea;
        this.idMedida = idMedida;
        this.idPresentacion = idPresentacion;
        this.idProveedor = idProveedor;
        this.fecha_compra = fecha_compra;
        this.fecha_vencimiento = fecha_vencimiento;
        this.fecha_baja = fecha_baja;
        this.fecha_actPrecios = fecha_actPrecios;
        this.idUbicacion = idUbicacion;
        this.fila = fila;
        this.columna = columna;
        this.precio_costo = precio_costo;
        this.margen_utilidad = margen_utilidad;
        this.idAlicuota = idAlicuota;
        this.es_bonificado = es_bonificado;
        this.bonificacion = bonificacion;
        this.estado = estado;
        this.nroLote = nroLote;
        this.ganancia = ganancia;
        this.precioFinalSinIva = precioFinalSinIva;
    }

    public int getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(int idArticulo) {
        this.idArticulo = idArticulo;
    }

    public String getCodigo_interno() {
        return codigo_interno;
    }

    public void setCodigo_interno(String codigo_interno) {
        this.codigo_interno = codigo_interno;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public TipoArticuloEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoArticuloEnum tipo) {
        this.tipo = tipo;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStock_minimo() {
        return stock_minimo;
    }

    public void setStock_minimo(int stock_minimo) {
        this.stock_minimo = stock_minimo;
    }

    public int getStock_maximo() {
        return stock_maximo;
    }

    public void setStock_maximo(int stock_maximo) {
        this.stock_maximo = stock_maximo;
    }

    public Lineas getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Lineas idLinea) {
        this.idLinea = idLinea;
    }

    public Medidas getIdMedida() {
        return idMedida;
    }

    public void setIdMedida(Medidas idMedida) {
        this.idMedida = idMedida;
    }

    public Presentaciones getIdPresentacion() {
        return idPresentacion;
    }

    public void setIdPresentacion(Presentaciones idPresentacion) {
        this.idPresentacion = idPresentacion;
    }

    public Proveedores getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(Proveedores idProveedor) {
        this.idProveedor = idProveedor;
    }

    public LocalDate getFecha_compra() {
        return fecha_compra;
    }

    public void setFecha_compra(LocalDate fecha_compra) {
        this.fecha_compra = fecha_compra;
    }

    public LocalDate getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public LocalDate getFecha_baja() {
        return fecha_baja;
    }

    public void setFecha_baja(LocalDate fecha_baja) {
        this.fecha_baja = fecha_baja;
    }

    public LocalDate getFecha_actPrecios() {
        return fecha_actPrecios;
    }

    public void setFecha_actPrecios(LocalDate fecha_actPrecios) {
        this.fecha_actPrecios = fecha_actPrecios;
    }

    public Ubicaciones getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Ubicaciones idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public BigDecimal getPrecio_costo() {
        return precio_costo;
    }

    public void setPrecio_costo(BigDecimal precio_costo) {
        this.precio_costo = precio_costo;
    }

    public BigDecimal getMargen_utilidad() {
        return margen_utilidad;
    }

    public void setMargen_utilidad(BigDecimal margen_utilidad) {
        this.margen_utilidad = margen_utilidad;
    }

    public boolean isEs_bonificado() {
        return es_bonificado;
    }

    public void setEs_bonificado(boolean es_bonificado) {
        this.es_bonificado = es_bonificado;
    }

    public BigDecimal getBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(BigDecimal bonificacion) {
        this.bonificacion = bonificacion;
    }

    public EstadoArticuloEnum getEstado() {
        return estado;
    }

    public void setEstado(EstadoArticuloEnum estado) {
        this.estado = estado;
    }

    public String getNroLote() {
        return nroLote;
    }

    public void setNroLote(String nroLote) {
        this.nroLote = nroLote;
    }

    public Rubros getRubros() {
        if (idLinea != null) {
            return idLinea.getRubros();
        }
        return rubros;
    }

    public void setRubros(Rubros rubros) {
        this.rubros = rubros;
    }

    public Depositos getDeposito() {
        if (idUbicacion != null) {
            return idUbicacion.getDepositos();
        }
        return deposito;
    }

    public void setDeposito(Depositos deposito) {
        this.deposito = deposito;
    }

    public BigDecimal getGanancia() {
        return ganancia;
    }

    public void setGanancia(BigDecimal ganancia) {
        this.ganancia = ganancia;

    }

    public BigDecimal getPrecioFinalSinIva() {
        return precioFinalSinIva;
    }

    public void setPrecioFinalSinIva(BigDecimal precioFinalSinIva) {
        this.precioFinalSinIva = precioFinalSinIva;
    }

    public BigDecimal getPrecioFinalConIva() {
        if (precioFinalSinIva != null) {
            BigDecimal alic = idAlicuota.getDescripcion().contains("%")
                    ? BigDecimal.valueOf(Double
                            .valueOf(idAlicuota.getDescripcion()
                                    .replace("%", "")))
                    : BigDecimal.ZERO;
            BigDecimal resConIva = precioFinalSinIva.multiply(alic)
                    .divide(BigDecimal.valueOf(100));
            return precioFinalSinIva.add(resConIva);
        }
        return BigDecimal.ZERO;
    }

    public void setPrecioFinalConIva(BigDecimal precioFinalConIva) {
        this.precioFinalConIva = precioFinalConIva;
    }

    public Alicuotas getIdAlicuota() {
        return idAlicuota;
    }

    public void setIdAlicuota(Alicuotas idAlicuota) {
        this.idAlicuota = idAlicuota;
    }

    public String getCodigo_barra() {
        return codigo_barra;
    }

    public void setCodigo_barra(String codigo_barra) {
        this.codigo_barra = codigo_barra;
    }

}
