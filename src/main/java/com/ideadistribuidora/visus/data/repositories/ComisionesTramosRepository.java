package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.ComisionesTramos;

import java.util.Set;

public interface ComisionesTramosRepository extends JpaRepository<ComisionesTramos, Integer>, JpaSpecificationExecutor<ComisionesTramos> { 

    Set<ComisionesTramos> findComisionesTramosByIdVendedor(int idVendedor);

}
