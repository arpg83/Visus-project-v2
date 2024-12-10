package com.ideadistribuidora.visus.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Depositos;
import com.ideadistribuidora.visus.data.Ubicaciones;

public interface UbicacionesRepository
                extends JpaRepository<Ubicaciones, Integer>, JpaSpecificationExecutor<Ubicaciones> {

        List<Ubicaciones> findUbicacionesByDepositos(Depositos selectedDepositos);

}
