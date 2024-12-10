package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Presentaciones;

public interface PresentacionesRepository
        extends JpaRepository<Presentaciones, Integer>, JpaSpecificationExecutor<Presentaciones> {

}
