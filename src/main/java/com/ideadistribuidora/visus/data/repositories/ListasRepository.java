package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Listas;

@Repository
public interface ListasRepository extends JpaRepository<Listas, Integer>, JpaSpecificationExecutor<Listas> {

}
