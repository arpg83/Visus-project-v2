package com.ideadistribuidora.visus.data.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideadistribuidora.visus.data.Articulos;

public interface ArticulosRepository extends JpaRepository<Articulos, Integer>, JpaSpecificationExecutor<Articulos> {
    @Query("SELECT new com.ideadistribuidora.visus.data.Articulos(a.idArticulo, a.codigo_interno,"
            +
            "a.codigo_barra, a.descripcion,  a.tipo,  a.stock,  a.stock_minimo,"
            +
            "a.stock_maximo, a.idLinea,  a.idMedida," +
            "a.idPresentacion,  a.idProveedor,  a.fecha_compra," +
            "a.fecha_vencimiento, a.fecha_baja,  a.fecha_actPrecios,"
            +
            "a.idUbicacion, a.fila,  a.columna," +
            "a.precio_costo,  a.margen_utilidad,  a.idAlicuota," +
            "a.es_bonificado, a.bonificacion,  a.estado, a.nroLote, " +
            "(a.precio_costo * (a.margen_utilidad / 100)), " +
            "(a.precio_costo + (a.precio_costo * (a.margen_utilidad / 100))) " +
            ") FROM Articulos a")
    List<Articulos> findAllWithGanancia();

    @Query("SELECT new com.ideadistribuidora.visus.data.Articulos(a.idArticulo, a.codigo_interno,"
            +
            "a.codigo_barra, a.descripcion,  a.tipo,  a.stock,  a.stock_minimo,"
            +
            "a.stock_maximo, a.idLinea,  a.idMedida," +
            "a.idPresentacion,  a.idProveedor,  a.fecha_compra," +
            "a.fecha_vencimiento, a.fecha_baja,  a.fecha_actPrecios,"
            +
            "a.idUbicacion, a.fila,  a.columna," +
            "a.precio_costo,  a.margen_utilidad,  a.idAlicuota," +
            "a.es_bonificado, a.bonificacion,  a.estado, a.nroLote, " +
            "(a.precio_costo * (a.margen_utilidad / 100)), " +
            "(a.precio_costo + (a.precio_costo * (a.margen_utilidad / 100))) " +
            ") FROM Articulos a " +
            "WHERE a.idArticulo = :id")
    Optional<Articulos> finByIdWithGanancia(@Param ("id")int id);

}
