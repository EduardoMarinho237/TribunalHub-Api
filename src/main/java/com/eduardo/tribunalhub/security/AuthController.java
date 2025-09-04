package com.eduardo.tribunalhub.security;

import com.eduardo.tribunalhub.app.usuario.Usuario;
import com.eduardo.tribunalhub.app.usuario.UsuarioRepository;
import com.eduardo.tribunalhub.security.dto.LoginRequest;
import com.eduardo.tribunalhub.security.dto.LoginResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepository;

    public AuthController(AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil,
                         UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getSenha()
                )
            );

            CustomUserDetailsService.CustomUserPrincipal userPrincipal = 
                (CustomUserDetailsService.CustomUserPrincipal) authentication.getPrincipal();
            
            Usuario usuario = userPrincipal.getUsuario();

            String token = jwtUtil.generateToken(
                usuario.getEmail(),
                usuario.getTipoUsuario().name(),
                usuario.getId()
            );

            LoginResponse response = new LoginResponse(
                token,
                "Bearer",
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo().name()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                .body("Credenciais inválidas");
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body("Erro na autenticação: " + e.getMessage());
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                if (jwtUtil.validateToken(token)) {
                    String username = jwtUtil.extractUsername(token);
                    Long userId = jwtUtil.extractUserId(token);
                    
                    // Get user details to return cargo
                    Usuario usuario = usuarioRepository.findByEmail(username)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                    return ResponseEntity.ok(new LoginResponse(
                        token,
                        "Bearer",
                        userId,
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getCargo().name()
                    ));
                }
            }
            return ResponseEntity.badRequest().body("Token inválido");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro na validação: " + e.getMessage());
        }
    }
}
