package com.ideadistribuidora.visus.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Lineas;
import com.ideadistribuidora.visus.data.Rubros;

public interface LineasRepository extends JpaRepository<Lineas, Integer>, JpaSpecificationExecutor<Lineas> {

    List<Lineas> findLineasByRubros(Rubros selectedRubros);

}
