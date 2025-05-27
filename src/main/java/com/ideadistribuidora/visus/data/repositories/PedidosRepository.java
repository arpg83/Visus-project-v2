package com.ideadistribuidora.visus.data.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Pedidos;
@Repository
public interface PedidosRepository extends JpaRepository<Pedidos, Integer>, JpaSpecificationExecutor<Pedidos> {
     @Query(value ="SELECT COALESCE(MAX(p.idPedido), 0) FROM Pedidos p", nativeQuery = true)
    Optional<Integer> getNextIdPedido();

}
