package com.tecsup.calificaciones.domain.port.in;

import com.tecsup.calificaciones.domain.model.Calificacion;
import java.util.List;

public interface CalificacionUseCase {
  Calificacion calificar(Long usuarioId, Long streamId, int puntuacion, String comentario);

  List<Calificacion> listarPorStream(Long streamId);
}
