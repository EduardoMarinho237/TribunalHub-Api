package com.eduardo.tribunalhub.app.cliente;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eduardo.tribunalhub.validacao.Email;
import com.eduardo.tribunalhub.validacao.ValidadorNaoExcluido;
import com.eduardo.tribunalhub.app.caso.CasoService;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final CasoService casoService;

    public ClienteService(ClienteRepository clienteRepository, CasoService CasoService) {
        this.clienteRepository = clienteRepository;
        this.casoService = CasoService;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientesVisiveis() {
        return clienteRepository.findByVisivelTrue();
    }

    @Transactional
    public Cliente registrarCliente(Cliente cliente) {
        if (cliente.getEmail() == null || cliente.getTelefone() == null || cliente.getNome() == null) {
            throw new IllegalArgumentException("Nome, email e telefone são obrigatórios");
        }
        if (clienteRepository.findByEmail(cliente.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        Email.validar(cliente.getEmail());
        cliente.setVisivel(true);
        return clienteRepository.save(cliente);
    }

    @Transactional
    public Optional<Cliente> atualizarCliente(Long id, Cliente clienteAtualizado) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        ValidadorNaoExcluido.validar(clienteExistente, Cliente::getVisivel, "Cliente");
        Email.validar(clienteAtualizado.getEmail());

        if (clienteAtualizado.getNome() != null) {
            clienteExistente.setNome(clienteAtualizado.getNome());
        }
        if (clienteAtualizado.getEmail() != null) {
            clienteExistente.setEmail(clienteAtualizado.getEmail());
        }
        if (clienteAtualizado.getTelefone() != null) {
            clienteExistente.setTelefone(clienteAtualizado.getTelefone());
        }
        if (clienteAtualizado.getAcompanhamento() != null) {
            clienteExistente.setAcompanhamento(clienteAtualizado.getAcompanhamento());
        }
        return Optional.of(clienteRepository.save(clienteExistente));
    }

    @Transactional
    public boolean excluirCliente(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        ValidadorNaoExcluido.validar(cliente, Cliente::getVisivel, "Cliente");

        cliente.setVisivel(false);
        clienteRepository.save(cliente);
        return true;
    }

    @Transactional(readOnly = true)
    public List<Cliente> listarClientesPorUsuario(Long usuarioId) {
        return clienteRepository.findAllByUsuarioIdAndVisivelTrue(usuarioId);
    }

    @Transactional(readOnly = true)
    public Optional<Cliente> buscarClientePorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));
        ValidadorNaoExcluido.validar(cliente, Cliente::getVisivel, "Cliente");
        return Optional.of(cliente);
    }

    @Transactional(readOnly = true)
    public Long buscarQuantidadeDeCasos(Long id) {
        Long quantidadeCasos = casoService.contarCasosPorCliente(id);
        return quantidadeCasos;
    }
}
