package com.eduardo.tribunalhub.app.caso;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CasoService {
    private final CasoRepository casoRepository;

    public CasoService(CasoRepository casoRepository) {
        this.casoRepository = casoRepository;
    }

    @Transactional(readOnly = true)
    public Long contarCasosPorCliente(Long clienteId) {
        return casoRepository.countByClienteIdAndVisivelTrue(clienteId);
    }
}