package com.eduardo.tribunalhub.security;

import com.eduardo.tribunalhub.app.usuario.Usuario;
import com.eduardo.tribunalhub.app.usuario.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com email: " + email));

        if (!usuario.getVisivel()) {
            throw new UsernameNotFoundException("Usuário inativo: " + email);
        }

        return new CustomUserPrincipal(usuario);
    }

    public static class CustomUserPrincipal implements UserDetails {
        private final Usuario usuario;

        public CustomUserPrincipal(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + usuario.getTipoUsuario().name()));
            return authorities;
        }

        @Override
        public String getPassword() {
            return usuario.getSenha();
        }

        @Override
        public String getUsername() {
            return usuario.getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return usuario.getVisivel();
        }

        public Usuario getUsuario() {
            return usuario;
        }
    }
}
