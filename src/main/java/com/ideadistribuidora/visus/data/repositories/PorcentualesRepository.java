package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Porcentuales;

public interface PorcentualesRepository extends JpaRepository<Porcentuales, Integer>, JpaSpecificationExecutor<Porcentuales>  {

}
