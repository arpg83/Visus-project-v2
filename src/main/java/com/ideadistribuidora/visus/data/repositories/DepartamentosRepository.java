package com.ideadistribuidora.visus.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Provincias;

public interface DepartamentosRepository
        extends JpaRepository<Departamentos, Integer>, JpaSpecificationExecutor<Departamentos> {

    List<Departamentos> findDptoByProvincias(Provincias provincia);

}
