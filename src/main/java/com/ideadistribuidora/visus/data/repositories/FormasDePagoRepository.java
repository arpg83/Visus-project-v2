package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.FormasDePago;

public interface FormasDePagoRepository extends JpaRepository<FormasDePago, Integer>, JpaSpecificationExecutor<FormasDePago> {

}
