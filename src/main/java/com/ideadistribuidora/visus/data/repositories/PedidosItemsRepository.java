package com.ideadistribuidora.visus.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.PedidosItems;

@Repository
public interface PedidosItemsRepository extends JpaRepository<PedidosItems, Integer>, JpaSpecificationExecutor<PedidosItems> {
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar pedidos por cliente, fecha, etc.



}
