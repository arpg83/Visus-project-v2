package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.TransportistasBancos;
import com.ideadistribuidora.visus.data.TransportistasBancosId;

public interface TransportistasBancosRepository extends JpaRepository<TransportistasBancos, TransportistasBancosId>, JpaSpecificationExecutor<TransportistasBancos> {
    void deleteTransportistasBancosById(TransportistasBancosId bancosId);
}
