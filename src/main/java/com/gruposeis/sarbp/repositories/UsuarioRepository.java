package com.gruposeis.sarbp.repositories;

import com.gruposeis.sarbp.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsername(String username);

    // Para que el admin liste solo los líderes
    List<Usuario> findByRol(Usuario.Rol rol);
}
