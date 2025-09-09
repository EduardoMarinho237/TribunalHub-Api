package com.eduardo.tribunalhub.app.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.eduardo.tribunalhub.armazenamento.ArmazenamentoService;
import com.eduardo.tribunalhub.validacao.Email;
import com.eduardo.tribunalhub.validacao.ValidadorNaoExcluido;
import com.eduardo.tribunalhub.app.usuario.enums.TipoUsuario;

import java.util.Optional;
import java.util.List;
import java.util.Objects;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArmazenamentoService armazenamentoService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder,
                          ArmazenamentoService armazenamentoService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.armazenamentoService = armazenamentoService;
    }

    @Transactional(readOnly = true)
    public List<Usuario> listarUsuariosAtivos() {
        return usuarioRepository.findByVisivelTrue();
    }

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }
        Email.validar(usuario.getEmail());
        ValidadorSenha.validar(usuario.getSenha());
        usuario.setTipoUsuario(TipoUsuario.COMUM);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Optional<Usuario> atualizarUsuario(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ValidadorNaoExcluido.validar(usuarioExistente, Usuario::getVisivel, "Usuário");
        Email.validar(usuarioAtualizado.getEmail());

        if (usuarioAtualizado.getNome() != null) {
            usuarioExistente.setNome(usuarioAtualizado.getNome());
        }
        if (usuarioAtualizado.getEmail() != null) {
            usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        }
        if (usuarioAtualizado.getCargo() != null) {
            usuarioExistente.setCargo(usuarioAtualizado.getCargo());
        }
        return Optional.of(usuarioRepository.save(usuarioExistente));
    }

    @Transactional
    public boolean excluirUsuario(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ValidadorNaoExcluido.validar(usuario, Usuario::getVisivel, "Usuário");

        usuarioRepository.excluirUsuario(id);
        return true;
    }

    @Transactional
    public boolean atualizarSenha(Long id, String senhaAtual, String novaSenha) {
        Objects.requireNonNull(senhaAtual, "Senha atual não pode ser nula");
        Objects.requireNonNull(novaSenha, "Nova senha não pode ser nula");
        ValidadorSenha.validar(novaSenha);

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ValidadorNaoExcluido.validar(usuario, Usuario::getVisivel, "Usuário");

        if (!passwordEncoder.matches(senhaAtual, usuario.getSenha())) {
            throw new IllegalArgumentException("Senha atual incorreta");
        }
        if (passwordEncoder.matches(novaSenha, usuario.getSenha())) {
            throw new IllegalArgumentException("A nova senha deve ser diferente da atual");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuarioRepository.save(usuario);
        return true;
    }

    @Transactional(readOnly = true)
    public Optional<String> buscarFotoPerfil(Long id) {
        return usuarioRepository.findFotoUrlById(id);
    }

    @Transactional
    public Optional<Usuario> atualizarFotoPerfil(Long id, MultipartFile arquivo) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));
        ValidadorNaoExcluido.validar(usuario, Usuario::getVisivel, "Usuário");

        if (usuario.getFotoUrl() != null) {
            armazenamentoService.excluirArquivo(usuario.getFotoUrl());
        }
        String fotoUrl = armazenamentoService.salvarArquivo(arquivo);
        usuario.setFotoUrl(fotoUrl);
        return Optional.of(usuarioRepository.save(usuario));
    }
}
