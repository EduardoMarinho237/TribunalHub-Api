package com.eduardo.tribunalhub.app.caso;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import com.eduardo.tribunalhub.app.caso.enums.TipoCaso;
import com.eduardo.tribunalhub.app.caso.enums.Tribunal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean visivel = true;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCaso tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tribunal tribunal;

    @Column(nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(nullable = false)
    private Long clienteId;

}
