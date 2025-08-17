package com.eduardo.tribunalhub.app.contrato;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }

    @PostMapping
    public ResponseEntity<Contrato> criarContrato(@RequestBody Contrato contrato) {
        Contrato novoContrato = contratoService.criarContrato(contrato);
        return ResponseEntity.ok(novoContrato);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contrato> atualizarContrato(
            @PathVariable Long id,
            @RequestBody Contrato contratoAtualizado) {
        return contratoService.atualizarContrato(id, contratoAtualizado)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contrato> buscarContratoPorId(@PathVariable Long id) {
        return contratoService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Contrato> listarTodosContratos() {
        return contratoService.listarTodos();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarContrato(@PathVariable Long id) {
        return contratoService.cancelarContrato(id)
                .map(contrato -> ResponseEntity.noContent().<Void>build())
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/situacao/{situacao}")
    public List<Contrato> listarPorSituacao(@PathVariable Contrato.SituacaoPagamento situacao) {
        return contratoService.listarPorSituacao(situacao);
    }

    @PostMapping("/verificar-expirados")
    public ResponseEntity<String> verificarContratosExpirados() {
        int quantidadeAtualizada = contratoService.verificarEAtualizarContratosExpirados();
        return ResponseEntity.ok(quantidadeAtualizada + " contratos atualizados para PENDENTE.");
    }

    @GetMapping("/{id}/situacao")
    public ResponseEntity<String> verificarSituacaoContrato(@PathVariable Long id) {
        return contratoService.verificarSituacaoContrato(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}