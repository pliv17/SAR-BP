package com.gruposeis.sarbp.repositories;

import com.gruposeis.sarbp.models.Alerta;
import com.gruposeis.sarbp.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    // Para el tablón público: solo alertas ACTIVO ordenadas por más recientes
    List<Alerta> findByEstadoOrderByFechaCreacionDesc(String estado);

    // Para el líder: solo SUS alertas, ordenadas por más recientes
    List<Alerta> findByUsuarioOrderByFechaCreacionDesc(Usuario usuario);
}
