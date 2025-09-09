package com.eduardo.tribunalhub.app.usuario.enums;

public enum Cargo {
    ADVOGADO,
    DESENVOLVEDOR,
    ESTAGIARIO,
    ASSISTENTE,
    PRESTADOR_SERVICO;
    
    public String getDescricao() {
        switch (this) {
            case ADVOGADO: return "Advogado";
            case DESENVOLVEDOR: return "Desenvolvedor";
            case ESTAGIARIO: return "Estagiário";
            case ASSISTENTE: return "Assistente";
            case PRESTADOR_SERVICO: return "Prestador de serviço";
            default: return this.name();
        }
    }
}