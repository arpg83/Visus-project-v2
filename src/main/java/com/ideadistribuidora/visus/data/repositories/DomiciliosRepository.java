package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Domicilios;

public interface DomiciliosRepository extends JpaRepository<Domicilios, Integer>, JpaSpecificationExecutor<Domicilios> {

}
