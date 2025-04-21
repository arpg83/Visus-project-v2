package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.ProveedoresBancos;
import com.ideadistribuidora.visus.data.ProveedoresBancosId;

@Repository
public interface ProveedoresBancosRepository
        extends JpaRepository<ProveedoresBancos, ProveedoresBancosId>, JpaSpecificationExecutor<ProveedoresBancos> {

    void deleteProveedoresBancosById(ProveedoresBancosId bancosId);

}
