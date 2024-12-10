package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Depositos;

public interface DepositosRepository extends JpaRepository<Depositos, Integer>, JpaSpecificationExecutor<Depositos> {

    String getDescripcionByIdDeposito(int id);

}
