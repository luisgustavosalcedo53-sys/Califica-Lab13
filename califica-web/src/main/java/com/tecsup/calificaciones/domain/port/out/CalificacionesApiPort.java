package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Calificacion;
import java.util.List;

/** Puerto de salida hacia calificaciones-service (antes: CalificacionRepositoryPort con JPA). */
public interface CalificacionesApiPort {
  Calificacion calificar(Long usuarioId, Long streamId, int puntuacion, String comentario);

  List<Calificacion> listarPorStream(Long streamId);
}
