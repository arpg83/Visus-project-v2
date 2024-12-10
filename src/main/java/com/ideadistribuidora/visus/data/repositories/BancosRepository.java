package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Bancos;

public interface BancosRepository extends JpaRepository<Bancos, Integer>, JpaSpecificationExecutor<Bancos> {

}
