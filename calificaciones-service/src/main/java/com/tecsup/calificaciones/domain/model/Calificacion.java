package com.tecsup.calificaciones.domain.model;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import java.time.LocalDateTime;

public record Calificacion(
    Long id,
    Long usuarioId,
    Long streamId,
    int puntuacion,
    String comentario,
    LocalDateTime fecha) {

  public static final int MINIMO = 1;
  public static final int MAXIMO = 5;

  public Calificacion {
    if (puntuacion < MINIMO || puntuacion > MAXIMO)
      throw new ReglaNegocioException("La puntuación debe estar entre 1 y 5");
    comentario = comentario == null ? "" : comentario.trim();
    if (comentario.length() > 280)
      throw new ReglaNegocioException("El comentario admite como máximo 280 caracteres");
  }

  public static Calificacion nueva(
      Long usuarioId, Long streamId, int puntuacion, String comentario) {
    return new Calificacion(null, usuarioId, streamId, puntuacion, comentario, LocalDateTime.now());
  }
}
