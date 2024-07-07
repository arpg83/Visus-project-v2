package com.ideadistribuidora.visus.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProveedoresRepository extends JpaRepository<Proveedores, Long>, JpaSpecificationExecutor<Proveedores> {

}
