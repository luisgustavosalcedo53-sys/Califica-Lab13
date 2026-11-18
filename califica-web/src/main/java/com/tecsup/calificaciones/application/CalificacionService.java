package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.model.Calificacion;
import com.tecsup.calificaciones.domain.port.in.CalificacionUseCase;
import com.tecsup.calificaciones.domain.port.out.CalificacionesApiPort;
import java.util.List;

/** Delegado simple: las reglas de calificación viven en calificaciones-service. */
public class CalificacionService implements CalificacionUseCase {

  private final CalificacionesApiPort calificaciones;

  public CalificacionService(CalificacionesApiPort calificaciones) {
    this.calificaciones = calificaciones;
  }

  @Override
  public Calificacion calificar(Long usuarioId, Long streamId, int puntuacion, String comentario) {
    return calificaciones.calificar(usuarioId, streamId, puntuacion, comentario);
  }

  @Override
  public List<Calificacion> listarPorStream(Long streamId) {
    return calificaciones.listarPorStream(streamId);
  }
}
