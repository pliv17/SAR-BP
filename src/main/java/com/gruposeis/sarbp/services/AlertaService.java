package com.gruposeis.sarbp.services;

import com.gruposeis.sarbp.models.Alerta;
import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.repositories.AlertaRepository;
import com.gruposeis.sarbp.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final UsuarioRepository usuarioRepository;

    public AlertaService(AlertaRepository alertaRepository, UsuarioRepository usuarioRepository) {
        this.alertaRepository = alertaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Alerta> listActiveAlerts() {
        return alertaRepository.findByEstado("ACTIVO");
    }

    public List<Alerta> listByUsuario(Long usuarioId) {
        return alertaRepository.findByUsuarioId(usuarioId);
    }

    public Optional<Alerta> findById(Long id) {
        return alertaRepository.findById(id);
    }

    public Alerta create(Alerta alerta, Long usuarioId) {
        Usuario u = usuarioRepository.findById(usuarioId).orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        alerta.setUsuario(u);
        return alertaRepository.save(alerta);
    }

    public Alerta update(Alerta alerta) {
        return alertaRepository.save(alerta);
    }

    public void delete(Long id) {
        alertaRepository.deleteById(id);
    }
}
