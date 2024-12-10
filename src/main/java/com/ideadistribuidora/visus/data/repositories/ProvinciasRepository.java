package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Provincias;

public interface ProvinciasRepository extends JpaRepository<Provincias, Integer>, JpaSpecificationExecutor<Provincias> {

}
