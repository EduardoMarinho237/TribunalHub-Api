package com.eduardo.tribunalhub.app.contrato;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.eduardo.tribunalhub.app.contrato.Contrato.SituacaoPagamento;

import java.util.List;
import java.util.Date;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    List<Contrato> findBySituacaoPagamento(SituacaoPagamento situacao);

    @Modifying
    @Query("UPDATE Contrato c SET c.situacaoPagamento = :novaSituacao WHERE c.id = :id")
    void atualizarSituacao(@Param("id") Long id, @Param("novaSituacao") SituacaoPagamento novaSituacao);

    @Query("SELECT c FROM Contrato c WHERE c.situacaoPagamento != 'CANCELADO'")
    List<Contrato> findContratosAtivos();

    @Query("SELECT c FROM Contrato c WHERE c.situacaoPagamento = 'TESTANDO' AND c.dataCriacao <= :dataLimite")
    List<Contrato> findContratosTestandoExpirados(@Param("dataLimite") Date dataLimite);
}