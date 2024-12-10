package com.ideadistribuidora.visus.data.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideadistribuidora.visus.data.Domicilios;
import com.ideadistribuidora.visus.data.Proveedores;
import com.ideadistribuidora.visus.data.ProveedoresBancos;

public interface ProveedoresRepository
        extends JpaRepository<Proveedores, Integer>, JpaSpecificationExecutor<Proveedores> {

    @Query("SELECT p.domicilios FROM Proveedores p WHERE p.idProveedor = :idProveedor")
    Set<Domicilios> findDomiciliosByIdProveedor(@Param("idProveedor") int idProveedor);

    @Query("SELECT p.proveedoresBancos FROM Proveedores p WHERE p.idProveedor = :idProveedor")
    Set<ProveedoresBancos> findBancosByIdProveedor(@Param("idProveedor") int idProveedor);

}
