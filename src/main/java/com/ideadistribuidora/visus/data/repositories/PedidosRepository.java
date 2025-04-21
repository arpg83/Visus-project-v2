package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Pedidos;
@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer>, JpaSpecificationExecutor<Pedidos> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar pedidos por cliente, fecha, etc.

}
