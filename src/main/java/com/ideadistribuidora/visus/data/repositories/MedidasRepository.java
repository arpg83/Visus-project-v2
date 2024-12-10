package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.ideadistribuidora.visus.data.Medidas;

public interface MedidasRepository extends JpaRepository<Medidas, Integer>, JpaSpecificationExecutor<Medidas> {

}
