package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ideadistribuidora.visus.data.Departamentos;
import com.ideadistribuidora.visus.data.Localidades;

public interface LocalidadesRepository
                extends JpaRepository<Localidades, Integer>, JpaSpecificationExecutor<Localidades> {

        void deleteLocalidadByDepartamentos(Departamentos departamento);

}
