package com.eduardo.tribunalhub.app.caso.model;

import com.eduardo.tribunalhub.app.caso.model.enums.StatusCaso;
import com.eduardo.tribunalhub.app.caso.model.enums.TipoCaso;
import com.eduardo.tribunalhub.app.caso.model.enums.Tribunal;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Caso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCaso tipo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Tribunal tribunal;

    @Column(nullable = false)
    private LocalDateTime dataAbertura;

    private LocalDateTime dataFinalizacao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCaso status = StatusCaso.ABERTO;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private Boolean acompanhamento = false;

    @Column(nullable = false)
    private Long clienteId;

    @ElementCollection
    @CollectionTable(name = "caso_dados_aprovados", 
                    joinColumns = @JoinColumn(name = "caso_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> dadosAprovados = new HashMap<>();

    @OneToMany(mappedBy = "caso", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Formulario> formulariosPendentes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        dataAbertura = LocalDateTime.now();
        if (status == null) {
            status = StatusCaso.ABERTO;
        }
    }

    public void fecharCaso() {
        this.status = StatusCaso.FECHADO;
        this.dataFinalizacao = LocalDateTime.now();
    }

    public void arquivarCaso() {
        this.status = StatusCaso.ARQUIVADO;
        this.dataFinalizacao = LocalDateTime.now();
    }
}
