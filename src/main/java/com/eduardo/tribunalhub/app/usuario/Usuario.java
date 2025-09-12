package com.eduardo.tribunalhub.app.usuario;

import com.eduardo.tribunalhub.app.contrato.Contrato;
import com.eduardo.tribunalhub.app.usuario.enums.Cargo;
import com.eduardo.tribunalhub.app.usuario.enums.TipoUsuario;

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

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "cargo", nullable = false)
    private Cargo cargo;

    @Column(nullable = false)
    private String senha;

    private String fotoUrl;

    @ManyToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    public String getCargoDescricao() {
        return cargo != null ? cargo.getDescricao() : null;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.toLowerCase().trim() : null;
    }

}