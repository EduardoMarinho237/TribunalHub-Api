package com.eduardo.tribunalhub.app.cliente;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.eduardo.tribunalhub.validacao.Email;
import com.eduardo.tribunalhub.validacao.ValidadorNaoExcluido;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.eduardo.tribunalhub.security.CustomUserDetailsService.CustomUserPrincipal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarMeusClientes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        Long userId = userPrincipal.getUsuario().getId();

        List<Cliente> clientes = clienteService.listarClientesPorUsuario(userId);

        for (Cliente cliente : clientes) {
            Long quantidadeCasos = clienteService.buscarQuantidadeDeCasos(cliente.getId());
            cliente.setQuantidadeCasos(quantidadeCasos);
        }

        return ResponseEntity.ok(clientes);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerCliente(@RequestBody Cliente cliente) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
            Long userId = userPrincipal.getUsuario().getId();
            
            if (!userId.equals(cliente.getUsuario().getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Não autorizado");
            }
            
            Email.validar(cliente.getEmail());
            Cliente novoCliente = clienteService.registrarCliente(cliente);
            return ResponseEntity.ok(novoCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao registrar cliente");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("@validadorAutorizacao.isUsuarioProprietarioDoCliente(#id)")
    public ResponseEntity<?> atualizarCliente(@PathVariable Long id, @RequestBody Cliente clienteAtualizado) {
        try {
            Cliente clienteExistente = clienteService.buscarClientePorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
            if (clienteAtualizado.getEmail() != null) {
                Email.validar(clienteAtualizado.getEmail());
            }
            return clienteService.atualizarCliente(id, clienteAtualizado)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("@validadorAutorizacao.isUsuarioProprietarioDoCliente(#id)")
    public ResponseEntity<String> excluirCliente(@PathVariable Long id) {
        try {
            Cliente clienteExistente = clienteService.buscarClientePorId(id)
                    .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
            ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
            if (clienteService.excluirCliente(id)) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
