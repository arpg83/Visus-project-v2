package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.ProveedoresBancos;
import com.ideadistribuidora.visus.data.ProveedoresBancosId;

public interface ProveedoresBancosRepository
        extends JpaRepository<ProveedoresBancos, ProveedoresBancosId>, JpaSpecificationExecutor<ProveedoresBancos> {

    void deleteProveedoresBancosById(ProveedoresBancosId bancosId);

}
