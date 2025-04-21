package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.ListasPorcentuales;

@Repository
public interface ListasPorcentualesRepository extends JpaRepository<ListasPorcentuales, Integer> , JpaSpecificationExecutor<ListasPorcentuales> {
    // Add custom query methods if needed
}