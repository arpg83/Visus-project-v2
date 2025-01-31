package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Comisiones;

public interface ComisionesRepository extends JpaRepository<Comisiones, Integer>, JpaSpecificationExecutor<Comisiones> {

}
