package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Rubros;

public interface RubrosRepository extends JpaRepository<Rubros, Integer>, JpaSpecificationExecutor<Rubros> {

}
