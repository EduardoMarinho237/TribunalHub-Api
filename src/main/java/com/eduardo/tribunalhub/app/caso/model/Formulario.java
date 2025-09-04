package com.eduardo.tribunalhub.app.caso.model;

import com.eduardo.tribunalhub.app.caso.model.enums.StatusFormulario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Formulario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "formulario_campos", 
                    joinColumns = @JoinColumn(name = "formulario_id"))
    @MapKeyColumn(name = "chave")
    @Column(name = "valor")
    private Map<String, String> campos = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFormulario status = StatusFormulario.PENDENTE;

    @Column(nullable = false)
    private LocalDateTime dataEnvio;

    private LocalDateTime dataResposta;

    @ManyToOne
    @JoinColumn(name = "caso_id", nullable = false)
    private Caso caso;

    @PrePersist
    protected void onCreate() {
        dataEnvio = LocalDateTime.now();
    }
}
