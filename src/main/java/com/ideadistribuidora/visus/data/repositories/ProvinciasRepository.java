package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Provincias;

@Repository
public interface ProvinciasRepository extends JpaRepository<Provincias, Integer>, JpaSpecificationExecutor<Provincias> {

}
