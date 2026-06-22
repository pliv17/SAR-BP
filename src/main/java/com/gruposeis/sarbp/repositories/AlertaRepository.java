package com.gruposeis.sarbp.repositories;

import com.gruposeis.sarbp.models.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
    List<Alerta> findByEstado(String estado);
    List<Alerta> findByUsuarioId(Long usuarioId);
}
