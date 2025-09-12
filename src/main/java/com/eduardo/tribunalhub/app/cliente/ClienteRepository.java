package com.eduardo.tribunalhub.app.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findByIdAndVisivelTrue(Long id);
    boolean existsByEmailAndUsuarioId(String email, Long usuarioId);
    boolean existsByEmailAndUsuarioIdAndIdNot(String email, Long usuarioId, Long clienteId);
    List<Cliente> findAllByUsuarioIdAndVisivelTrue(Long usuarioId);
    List<Cliente> findAllByVisivelTrue();
    List<Cliente> findByVisivelTrue();

}
