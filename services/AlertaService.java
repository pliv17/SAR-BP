package com.gruposeis.sarbp.services;

import com.gruposeis.sarbp.models.Alerta;
import com.gruposeis.sarbp.models.Categoria;
import com.gruposeis.sarbp.models.Usuario;
import com.gruposeis.sarbp.repositories.AlertaRepository;
import com.gruposeis.sarbp.repositories.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final CategoriaRepository categoriaRepository;

    public AlertaService(AlertaRepository alertaRepository, CategoriaRepository categoriaRepository) {
        this.alertaRepository = alertaRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ── Vista pública: alertas ACTIVO más recientes ────────────────
    public List<Alerta> obtenerAlertasActivas() {
        return alertaRepository.findByEstadoOrderByFechaCreacionDesc("ACTIVO");
    }

    // ── Panel Líder: alertas del líder autenticado ─────────────────
    public List<Alerta> obtenerAlertasDelLider(Usuario usuario) {
        return alertaRepository.findByUsuarioOrderByFechaCreacionDesc(usuario);
    }

    // ── Panel Líder: crear nueva alerta ───────────────────────────
    public void crearAlerta(String titulo, String descripcion, Long categoriaId, Usuario lider) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));

        Alerta alerta = new Alerta();
        alerta.setTitulo(titulo);
        alerta.setDescripcion(descripcion);
        alerta.setCategoria(categoria);
        alerta.setUsuario(lider);
        alerta.setEstado("ACTIVO");

        alertaRepository.save(alerta);
    }

    // ── Panel Líder: eliminar alerta propia ───────────────────────
    // Valida que la alerta pertenezca al líder antes de eliminar
    public void eliminarAlerta(Long alertaId, Usuario lider) {
        Alerta alerta = alertaRepository.findById(alertaId)
                .orElseThrow(() -> new IllegalArgumentException("Alerta no encontrada"));

        if (!alerta.getUsuario().getId().equals(lider.getId())) {
            throw new SecurityException("No puedes eliminar una alerta que no es tuya");
        }

        alertaRepository.delete(alerta);
    }

    // ── Formulario: categorías activas para el dropdown ──────────
    public List<Categoria> obtenerCategoriasActivas() {
        return categoriaRepository.findByEstado("ACTIVO");
    }
}
