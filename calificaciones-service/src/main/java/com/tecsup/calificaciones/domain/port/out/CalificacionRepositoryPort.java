package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Calificacion;
import java.util.List;

public interface CalificacionRepositoryPort {
  Calificacion guardar(Calificacion calificacion);

  List<Calificacion> listarPorStream(Long streamId);

  boolean existe(Long usuarioId, Long streamId);
}
