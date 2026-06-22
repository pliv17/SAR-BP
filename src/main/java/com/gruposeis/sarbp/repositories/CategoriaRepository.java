package com.gruposeis.sarbp.repositories;

import com.gruposeis.sarbp.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    // Para el dropdown del formulario de nueva alerta
    List<Categoria> findByEstado(String estado);
}
