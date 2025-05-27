package com.ideadistribuidora.visus.data.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Pedidos;
import com.ideadistribuidora.visus.data.PedidosListas;

@Repository
public interface PedidosListasRepository extends JpaRepository<PedidosListas, Integer>, JpaSpecificationExecutor<PedidosListas> {

    List<PedidosListas> findPedidosListasByIdPedido(Pedidos pedidos);
    // Aquí puedes agregar métodos personalizados si es necesario
    // Por ejemplo, para buscar pedidos por cliente, fecha, etc.

}
