package com.eduardo.tribunalhub.app.caso.repository;

import com.eduardo.tribunalhub.app.caso.model.Caso;
import com.eduardo.tribunalhub.app.caso.model.enums.StatusCaso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Long> {
    
    List<Caso> findByClienteId(Long clienteId);
    
    List<Caso> findByStatus(StatusCaso status);
    
    List<Caso> findByClienteIdAndStatus(Long clienteId, StatusCaso status);
    
    @Query("SELECT c FROM Caso c WHERE c.acompanhamento = true")
    List<Caso> findCasosEmAcompanhamento();
    
    @Query("SELECT c FROM Caso c WHERE c.clienteId = :clienteId AND c.acompanhamento = true")
    List<Caso> findCasosEmAcompanhamentoPorCliente(@Param("clienteId") Long clienteId);
}
