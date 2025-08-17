package com.eduardo.tribunalhub.app.contrato;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ContratoService {

    private final ContratoRepository contratoRepository;

    public ContratoService(ContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
    }

    @Transactional
    public Contrato criarContrato(Contrato contrato) {
        if (contrato.getSituacaoPagamento() == null) {
            contrato.setSituacaoPagamento(Contrato.SituacaoPagamento.TESTANDO);
        }
        return contratoRepository.save(contrato);
    }

    @Transactional
    public Optional<Contrato> atualizarContrato(Long id, Contrato contratoAtualizado) {
        return contratoRepository.findById(id)
                .map(contrato -> {
                    contrato.setSituacaoPagamento(contratoAtualizado.getSituacaoPagamento());
                    return contratoRepository.save(contrato);
                });
    }

    public Optional<Contrato> buscarPorId(Long id) {
        return contratoRepository.findById(id);
    }

    public List<Contrato> listarTodos() {
        return contratoRepository.findAll();
    }

    @Transactional
    public Optional<Contrato> cancelarContrato(Long id) {
        return contratoRepository.findById(id)
                .map(contrato -> {
                    contrato.setSituacaoPagamento(Contrato.SituacaoPagamento.CANCELADO);
                    return contratoRepository.save(contrato);
                });
    }

    public List<Contrato> listarPorSituacao(Contrato.SituacaoPagamento situacao) {
        return contratoRepository.findBySituacaoPagamento(situacao);
    }

    @Transactional
    public int verificarEAtualizarContratosExpirados() {
        Date quinzeDiasAtras = DateUtils.addDays(new Date(), -15);
        
        List<Contrato> contratosExpirados = contratoRepository
                .findContratosTestandoExpirados(quinzeDiasAtras);

        contratosExpirados.forEach(contrato -> {
            contrato.setSituacaoPagamento(Contrato.SituacaoPagamento.PENDENTE);
        });

        contratoRepository.saveAll(contratosExpirados);
        
        return contratosExpirados.size();
    }

    public Optional<String> verificarSituacaoContrato(Long id) {
        return contratoRepository.findById(id)
                .map(contrato -> "Situação do Contrato ID " + id + ": " + contrato.getSituacaoPagamento());
    }
}