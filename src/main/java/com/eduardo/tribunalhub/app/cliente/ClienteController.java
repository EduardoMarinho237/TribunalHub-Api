package com.eduardo.tribunalhub.app.cliente;

import com.eduardo.tribunalhub.exception.EmailJaExistenteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eduardo.tribunalhub.validacao.Email;
import com.eduardo.tribunalhub.validacao.ValidadorAutorizacao;
import com.eduardo.tribunalhub.validacao.ValidadorNaoExcluido;
import java.util.List;
import java.util.Map;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.eduardo.tribunalhub.security.CustomUserDetailsService.CustomUserPrincipal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final ValidadorAutorizacao validadorAutorizacao;

    public ClienteController(ClienteService clienteService, 
        ValidadorAutorizacao validadorAutorizacao) {
        this.clienteService = clienteService;
        this.validadorAutorizacao = validadorAutorizacao;
    }

    private Long getIdUsuarioLogado() {
        return validadorAutorizacao.obterIdUsuarioLogado();
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarMeusClientes() {
        Long idUsuarioLogado = getIdUsuarioLogado();
        List<Cliente> clientes = clienteService.listarClientesPorUsuario(idUsuarioLogado);

        for (Cliente cliente : clientes) {
            Long quantidadeCasos = clienteService.buscarQuantidadeDeCasos(cliente.getId());
            cliente.setQuantidadeCasos(quantidadeCasos);
        }

        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCliente(@RequestBody Cliente cliente) {
        try {
            Long idUsuarioLogado = getIdUsuarioLogado();
            if (!idUsuarioLogado.equals(cliente.getUsuario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("message","Não autorizado"));
            }
            
            Email.validar(cliente.getEmail());
            Cliente novoCliente = clienteService.registrarCliente(cliente, idUsuarioLogado);
            return ResponseEntity.ok(novoCliente);
        } catch (EmailJaExistenteException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message","Erro ao registrar cliente"));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@validadorAutorizacao.isUsuarioProprietarioDoCliente(#id)")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            Long idUsuarioLogado = getIdUsuarioLogado();
            Cliente clienteExistente = clienteService.buscarClientePorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
            if (clienteAtualizado.getEmail() != null) {
                Email.validar(clienteAtualizado.getEmail());
            }
            return clienteService.atualizarCliente(id, clienteAtualizado, idUsuarioLogado, id)
                    .map(c -> ResponseEntity.<Object>ok(c))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(Map.of("message", "Cliente não encontrado")));
        } catch (EmailJaExistenteException | IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message","Erro ao registrar cliente"));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@validadorAutorizacao.isUsuarioProprietarioDoCliente(#id)")
    public ResponseEntity<?> excluirCliente(@PathVariable Long id) {
        try {
            Cliente clienteExistente = clienteService.buscarClientePorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
            if (clienteService.excluirCliente(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("@validadorAutorizacao.isUsuarioProprietarioDoCliente(#id)")
    public ResponseEntity<?> buscarClientePorId(@PathVariable Long id) {
        try {
            Cliente clienteExistente = clienteService.buscarClientePorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
            return ResponseEntity.ok(clienteExistente);
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("message",e.getMessage()));
        }
    }
}
