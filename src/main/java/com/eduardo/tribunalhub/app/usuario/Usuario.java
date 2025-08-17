package com.eduardo.tribunalhub.app.usuario;

import com.eduardo.tribunalhub.app.contrato.Contrato;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean visivel = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private String senha;

    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    public enum TipoUsuario {
        COMUM,
        ADMIN,
        SUPER_ADMIN
    }

    public enum Cargo {
        ADVOGADO,
        DESENVOLVEDOR,
        PRESTADOR_SERVICO,
        ESTAGIARIO,
        ASSISTENTE
    }
}