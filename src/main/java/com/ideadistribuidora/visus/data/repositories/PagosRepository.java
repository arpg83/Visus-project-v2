package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Pagos;


@Repository
public interface PagosRepository extends JpaRepository<Pagos, Integer>, JpaSpecificationExecutor<Pagos> {

}
