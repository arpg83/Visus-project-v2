package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ideadistribuidora.visus.data.Coeficientes;

public interface CoeficientesRepository extends JpaRepository<Coeficientes, Integer>, JpaSpecificationExecutor<Coeficientes> {

}
