package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Alicuotas;
@Repository
public interface AlicuotasRepository extends JpaRepository<Alicuotas, Integer>, JpaSpecificationExecutor<Alicuotas> {

}
