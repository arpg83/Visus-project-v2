package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.ClientesBancos;
import com.ideadistribuidora.visus.data.ClientesBancosId;

@Repository
public interface ClientesBancosRepository
        extends JpaRepository<ClientesBancos, ClientesBancosId>, JpaSpecificationExecutor<ClientesBancos> {

    void deleteClientesBancosById(ClientesBancosId bancosId);

    // Consultar todas las relaciones por cliente ID
    // Set<ClientesBancos> findByIdCliente(int idCliente);
}
