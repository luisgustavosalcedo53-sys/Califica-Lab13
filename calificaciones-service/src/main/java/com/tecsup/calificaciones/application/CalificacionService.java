package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.model.Calificacion;
import com.tecsup.calificaciones.domain.port.in.CalificacionUseCase;
import com.tecsup.calificaciones.domain.port.out.CalificacionRepositoryPort;
import com.tecsup.calificaciones.domain.port.out.StreamConsultaPort;
import com.tecsup.calificaciones.domain.port.out.UsuarioConsultaPort;
import java.util.List;

public class CalificacionService implements CalificacionUseCase {

  private final CalificacionRepositoryPort calificaciones;
  private final UsuarioConsultaPort usuarios;
  private final StreamConsultaPort streams;

  public CalificacionService(
      CalificacionRepositoryPort calificaciones,
      UsuarioConsultaPort usuarios,
      StreamConsultaPort streams) {
    this.calificaciones = calificaciones;
    this.usuarios = usuarios;
    this.streams = streams;
  }

  @Override
  public Calificacion calificar(Long usuarioId, Long streamId, int puntuacion, String comentario) {
    if (usuarioId == null || !usuarios.existe(usuarioId))
      throw new ReglaNegocioException("Elige un usuario existente");
    if (streamId == null || !streams.existe(streamId))
      throw new ReglaNegocioException("El stream no existe");
    if (calificaciones.existe(usuarioId, streamId))
      throw new ReglaNegocioException("Este usuario ya calificó este stream");

    Calificacion nueva = Calificacion.nueva(usuarioId, streamId, puntuacion, comentario);
    return calificaciones.guardar(nueva);
  }

  @Override
  public List<Calificacion> listarPorStream(Long streamId) {
    return calificaciones.listarPorStream(streamId);
  }
}
