package com.gruposeis.sarbp.services;

import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.repositories.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ── Admin: listar todos los líderes ───────────────────────────
    public List<Usuario> listarLideres() {
        return usuarioRepository.findByRol(Usuario.Rol.LIDER);
    }

    // ── Admin: crear nuevo líder ──────────────────────────────────
    public void crearLider(String nombres, String apellidos, String docIdentificacion,
                           String username, String password) {
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El username ya está en uso");
        }
        Usuario nuevo = new Usuario();
        nuevo.setNombres(nombres);
        nuevo.setApellidos(apellidos);
        nuevo.setDocIdentificacion(docIdentificacion);
        nuevo.setUsername(username);
        nuevo.setPassword(passwordEncoder.encode(password));
        nuevo.setRol(Usuario.Rol.LIDER);
        nuevo.setEstado("ACTIVO");
        usuarioRepository.save(nuevo);
    }

    // ── Admin: activar/desactivar líder ───────────────────────────
    public void toggleEstado(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        if ("ACTIVO".equals(usuario.getEstado())) {
            usuario.setEstado("INACTIVO");
        } else {
            usuario.setEstado("ACTIVO");
        }
        usuarioRepository.save(usuario);
    }

    // ── Buscar usuario por username (para obtener el objeto Usuario desde Spring Security) ──
    public Usuario buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }
}
