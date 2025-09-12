package com.eduardo.tribunalhub.app.cliente;

import java.util.Date;
import org.hibernate.annotations.ColumnTransformer;

import com.eduardo.tribunalhub.app.usuario.Usuario;

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
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean visivel = true;
    
    @ColumnTransformer(write = "lower(?)")
    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String telefone;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean acompanhamento = false;
    
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false)
    private Date dataCriacao = new Date();

    @Transient
    private Long quantidadeCasos;

}
