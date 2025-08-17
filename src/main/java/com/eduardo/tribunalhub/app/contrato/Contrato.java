package com.eduardo.tribunalhub.app.contrato;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Contrato {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20) default 'TESTANDO'")
    private SituacaoPagamento situacaoPagamento = SituacaoPagamento.TESTANDO;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false)
    private Date dataCriacao = new Date();

    public enum SituacaoPagamento {
        TESTANDO,
        PENDENTE,
        PAGO,
        INSENTO,
        CANCELADO
    }
}