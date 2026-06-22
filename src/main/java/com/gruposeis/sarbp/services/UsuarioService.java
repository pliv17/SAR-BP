package com.gruposeis.sarbp.services;

import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public List<Usuario> listLeaders() {
        return usuarioRepository.findAll().stream()
                .filter(u -> u.getRol() == Usuario.Rol.LIDER)
                .toList();
    }

    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario save(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public void deactivate(Long id) {
        usuarioRepository.findById(id).ifPresent(u -> {
            u.setEstado("INACTIVO");
            usuarioRepository.save(u);
        });
    }
}
