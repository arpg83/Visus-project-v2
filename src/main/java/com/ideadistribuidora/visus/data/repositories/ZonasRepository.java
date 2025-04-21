package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Zonas;

@Repository
public interface  ZonasRepository extends JpaRepository<Zonas, Integer>, JpaSpecificationExecutor<Zonas>  {

}
