package com.eduardo.tribunalhub.app.caso;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CasoRepository extends JpaRepository<Caso, Long> {

    Long countByClienteIdAndVisivelTrue(Long clienteId);

}
