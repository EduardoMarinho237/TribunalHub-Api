package com.eduardo.tribunalhub.app.caso.repository;

import com.eduardo.tribunalhub.app.caso.model.Formulario;
import com.eduardo.tribunalhub.app.caso.model.enums.StatusFormulario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormularioRepository extends JpaRepository<Formulario, Long> {
    
    List<Formulario> findByCasoId(Long casoId);
    
    List<Formulario> findByStatus(StatusFormulario status);
    
    List<Formulario> findByCasoIdAndStatus(Long casoId, StatusFormulario status);
}
