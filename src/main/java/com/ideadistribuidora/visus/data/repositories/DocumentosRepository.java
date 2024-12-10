package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Documento;

public interface DocumentosRepository extends JpaRepository<Documento, Integer>, JpaSpecificationExecutor<Documento> {

}
