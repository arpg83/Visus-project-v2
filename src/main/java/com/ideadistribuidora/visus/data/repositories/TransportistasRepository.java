package com.ideadistribuidora.visus.data.repositories;

import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ideadistribuidora.visus.data.Transportistas;
import com.ideadistribuidora.visus.data.TransportistasBancos;

@Repository
public interface TransportistasRepository extends JpaRepository<Transportistas, Integer>, JpaSpecificationExecutor<Transportistas> {

    @Query("SELECT t.transportistasBancos FROM Transportistas t WHERE t.idTransportista = :idTransportista")
    Set<TransportistasBancos> findBancosByIdTransportista(@Param("idTransportista") int idTransportista);

    Optional<Transportistas> findByIdDocumentoAndNumeroDocumento(int idDocumento, Long numeroDocumento);
}
