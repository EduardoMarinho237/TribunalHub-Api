package com.eduardo.tribunalhub.app.usuario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
    List<Usuario> findByVisivelTrue();

    @Modifying
    @Query("UPDATE Usuario u SET u.senha = :novaSenha WHERE u.id = :id")
    void atualizarSenha(@Param("id") Long id, @Param("novaSenha") String novaSenha);

    @Modifying
    @Query("UPDATE Usuario u SET u.visivel = false WHERE u.id = :id")
    void excluirUsuario(@Param("id") Long id);

    @Query("SELECT u.fotoUrl FROM Usuario u WHERE u.id = :id AND u.visivel = true")
    Optional<String> findFotoUrlById(@Param("id") Long id);

}
