package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository;

import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioJpaRepository extends JpaRepository<UsuarioEntity, Long> {
  boolean existsByEmail(String email);

  List<UsuarioEntity> findAllByOrderByNombreAsc();
}
