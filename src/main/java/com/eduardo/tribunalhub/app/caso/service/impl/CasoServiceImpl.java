package com.eduardo.tribunalhub.app.caso.service.impl;

import com.eduardo.tribunalhub.app.caso.model.Caso;
import com.eduardo.tribunalhub.app.caso.model.Formulario;
import com.eduardo.tribunalhub.app.caso.model.enums.StatusCaso;
import com.eduardo.tribunalhub.app.caso.model.enums.StatusFormulario;
import com.eduardo.tribunalhub.app.caso.repository.CasoRepository;
import com.eduardo.tribunalhub.app.caso.repository.FormularioRepository;
import com.eduardo.tribunalhub.app.caso.service.CasoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class CasoServiceImpl implements CasoService {

    @Autowired
    private CasoRepository casoRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Override
    public Caso criarCaso(Caso caso) {
        if (caso.getDataAbertura() == null) {
            caso.setDataAbertura(LocalDateTime.now());
        }
        if (caso.getStatus() == null) {
            caso.setStatus(StatusCaso.ABERTO);
        }
        return casoRepository.save(caso);
    }

    @Override
    @Transactional(readOnly = true)
    public Caso buscarCasoPorId(Long id) {
        return casoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Caso não encontrado com ID: " + id));
    }

    @Override
    public Caso atualizarCaso(Long id, Caso casoAtualizado) {
        Caso casoExistente = buscarCasoPorId(id);
        
        casoExistente.setNome(casoAtualizado.getNome());
        casoExistente.setTipo(casoAtualizado.getTipo());
        casoExistente.setTribunal(casoAtualizado.getTribunal());
        casoExistente.setAcompanhamento(casoAtualizado.getAcompanhamento());
        
        // Não permite alterar status diretamente, use métodos específicos
        if (casoAtualizado.getStatus() != null && 
            !casoAtualizado.getStatus().equals(casoExistente.getStatus())) {
            if (casoAtualizado.getStatus() == StatusCaso.FECHADO) {
                casoExistente.fecharCaso();
            } else if (casoAtualizado.getStatus() == StatusCaso.ARQUIVADO) {
                casoExistente.arquivarCaso();
            } else {
                casoExistente.setStatus(casoAtualizado.getStatus());
            }
        }
        
        return casoRepository.save(casoExistente);
    }

    @Override
    public void deletarCaso(Long id) {
        Caso caso = buscarCasoPorId(id);
        casoRepository.delete(caso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Caso> listarCasosPorCliente(Long clienteId) {
        return casoRepository.findByClienteId(clienteId);
    }

    @Override
    public Formulario adicionarFormularioPendente(Long casoId, Map<String, String> campos) {
        Caso caso = buscarCasoPorId(casoId);
        
        Formulario formulario = new Formulario();
        formulario.setCaso(caso);
        formulario.setCampos(campos);
        formulario.setStatus(StatusFormulario.PENDENTE);
        formulario.setDataEnvio(LocalDateTime.now());
        
        return formularioRepository.save(formulario);
    }

    @Override
    public void aprovarCampoFormulario(Long casoId, Long formularioId, String chave) {
        Caso caso = buscarCasoPorId(casoId);
        Formulario formulario = formularioRepository.findById(formularioId)
                .orElseThrow(() -> new RuntimeException("Formulário não encontrado com ID: " + formularioId));
        
        if (!formulario.getCaso().getId().equals(casoId)) {
            throw new RuntimeException("Formulário não pertence ao caso especificado");
        }
        
        if (!formulario.getCampos().containsKey(chave)) {
            throw new RuntimeException("Campo não encontrado no formulário: " + chave);
        }
        
        // Move o campo aprovado para dadosAprovados do caso
        String valor = formulario.getCampos().get(chave);
        caso.getDadosAprovados().put(chave, valor);
        
        // Remove o campo do formulário
        formulario.getCampos().remove(chave);
        
        // Atualiza status do formulário
        if (formulario.getCampos().isEmpty()) {
            formulario.setStatus(StatusFormulario.APROVADO);
        } else {
            formulario.setStatus(StatusFormulario.APROVADO_PARCIALMENTE);
        }
        
        formulario.setDataResposta(LocalDateTime.now());
        
        casoRepository.save(caso);
        formularioRepository.save(formulario);
    }

    @Override
    public void removerInformacaoAprovada(Long casoId, String chave) {
        Caso caso = buscarCasoPorId(casoId);
        
        if (!caso.getDadosAprovados().containsKey(chave)) {
            throw new RuntimeException("Informação não encontrada nos dados aprovados: " + chave);
        }
        
        caso.getDadosAprovados().remove(chave);
        casoRepository.save(caso);
    }

    @Override
    public Caso fecharCaso(Long casoId) {
        Caso caso = buscarCasoPorId(casoId);
        caso.fecharCaso();
        return casoRepository.save(caso);
    }

    @Override
    public Caso arquivarCaso(Long casoId) {
        Caso caso = buscarCasoPorId(casoId);
        caso.arquivarCaso();
        return casoRepository.save(caso);
    }
}
