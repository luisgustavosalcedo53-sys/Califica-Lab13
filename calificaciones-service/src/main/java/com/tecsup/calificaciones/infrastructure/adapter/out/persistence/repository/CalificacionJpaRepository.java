package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository;

import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.CalificacionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalificacionJpaRepository extends JpaRepository<CalificacionEntity, Long> {
  List<CalificacionEntity> findByStreamIdOrderByFechaDesc(Long streamId);

  boolean existsByUsuarioIdAndStreamId(Long usuarioId, Long streamId);
}
