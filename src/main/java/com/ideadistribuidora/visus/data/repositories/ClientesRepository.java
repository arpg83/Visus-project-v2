package com.ideadistribuidora.visus.data.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ideadistribuidora.visus.data.Clientes;
import com.ideadistribuidora.visus.data.ClientesBancos;
import com.ideadistribuidora.visus.data.Domicilios;

public interface ClientesRepository extends JpaRepository<Clientes, Integer>, JpaSpecificationExecutor<Clientes> {
    // List<Clientes> findBySexo(SexoEnum sexo);
    @Query("SELECT c.domicilios FROM Clientes c WHERE c.idCliente = :idCliente")
    Set<Domicilios> findDomiciliosByIdCliente(@Param("idCliente") int idCliente);

    @Query("SELECT c.clientesBancos FROM Clientes c WHERE c.idCliente = :idCliente")
    Set<ClientesBancos> findBancosByIdCliente(@Param("idCliente") int idCliente);
}
