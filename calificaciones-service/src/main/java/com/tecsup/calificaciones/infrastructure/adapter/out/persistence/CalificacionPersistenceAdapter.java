package com.tecsup.calificaciones.infrastructure.adapter.out.persistence;

import com.tecsup.calificaciones.domain.model.Calificacion;
import com.tecsup.calificaciones.domain.port.out.CalificacionRepositoryPort;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.CalificacionEntity;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository.CalificacionJpaRepository;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class CalificacionPersistenceAdapter implements CalificacionRepositoryPort {

  private final CalificacionJpaRepository jpa;

  public CalificacionPersistenceAdapter(CalificacionJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Calificacion guardar(Calificacion calificacion) {
    return jpa.save(CalificacionEntity.desde(calificacion)).aDominio();
  }

  @Override
  public List<Calificacion> listarPorStream(Long streamId) {
    return jpa.findByStreamIdOrderByFechaDesc(streamId).stream()
        .map(CalificacionEntity::aDominio)
        .toList();
  }

  @Override
  public boolean existe(Long usuarioId, Long streamId) {
    return jpa.existsByUsuarioIdAndStreamId(usuarioId, streamId);
  }
}
