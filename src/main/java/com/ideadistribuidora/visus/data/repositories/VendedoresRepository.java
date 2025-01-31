package com.ideadistribuidora.visus.data.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideadistribuidora.visus.data.Vendedores;
import com.ideadistribuidora.visus.data.Zonas;

public interface VendedoresRepository extends JpaRepository<Vendedores, Integer>, JpaSpecificationExecutor<Vendedores> {
    Optional<Vendedores> findByIdDocumentoAndNumeroDeDocumento(int idDocumento, Long numeroDeDocumento);
    
    @Query("SELECT v.zonas FROM Vendedores v WHERE v.idVendedor = :idVendedor")
    Set<Zonas> findZonasByIdVendedor(@Param("idVendedor") int idVendedor);
}