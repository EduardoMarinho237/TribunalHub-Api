package com.eduardo.tribunalhub.validacao;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.eduardo.tribunalhub.security.CustomUserDetailsService.CustomUserPrincipal;
import com.eduardo.tribunalhub.app.cliente.ClienteService;
import com.eduardo.tribunalhub.app.cliente.Cliente;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ValidadorAutorizacao {

    @Autowired
    private ClienteService clienteService;

    public boolean isUsuarioProprietarioDoCliente(Long clienteId) {
        CustomUserPrincipal userPrincipal = obterUsuarioLogado();
        Long userId = userPrincipal.getUsuario().getId();

        Optional<Cliente> cliente = clienteService.buscarClientePorId(clienteId);
        return cliente.isPresent() && cliente.get().getUsuario().getId().equals(userId);
    }

    private CustomUserPrincipal obterUsuarioLogado() {
        return (CustomUserPrincipal) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    public Long obterIdUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        return userPrincipal.getUsuario().getId();
    }
}