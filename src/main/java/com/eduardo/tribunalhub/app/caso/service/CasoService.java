package com.eduardo.tribunalhub.app.caso.service;

import com.eduardo.tribunalhub.app.caso.model.Caso;
import com.eduardo.tribunalhub.app.caso.model.Formulario;

import java.util.List;
import java.util.Map;

public interface CasoService {
    
    Caso criarCaso(Caso caso);
    
    Caso buscarCasoPorId(Long id);
    
    Caso atualizarCaso(Long id, Caso caso);
    
    void deletarCaso(Long id);
    
    List<Caso> listarCasosPorCliente(Long clienteId);
    
    Formulario adicionarFormularioPendente(Long casoId, Map<String, String> campos);
    
    void aprovarCampoFormulario(Long casoId, Long formularioId, String chave);
    
    void removerInformacaoAprovada(Long casoId, String chave);
    
    Caso fecharCaso(Long casoId);
    
    Caso arquivarCaso(Long casoId);
}
