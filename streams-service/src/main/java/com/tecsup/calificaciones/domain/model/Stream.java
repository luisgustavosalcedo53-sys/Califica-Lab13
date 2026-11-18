package com.tecsup.calificaciones.domain.model;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;

/** Una transmisión que los usuarios pueden calificar. */
public record Stream(Long id, String titulo, String categoria) {

  public Stream {
    if (titulo == null || titulo.isBlank())
      throw new ReglaNegocioException("El título es obligatorio");
    titulo = titulo.trim();
    categoria = (categoria == null || categoria.isBlank()) ? "General" : categoria.trim();
  }

  public static Stream nuevo(String titulo, String categoria) {
    return new Stream(null, titulo, categoria);
  }
}
