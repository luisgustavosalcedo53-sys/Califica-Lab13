package com.tecsup.calificaciones.domain.model;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;

public record Usuario(Long id, String nombre, String email) {

  public Usuario {
    if (nombre == null || nombre.isBlank())
      throw new ReglaNegocioException("El nombre es obligatorio");
    if (email == null || !email.trim().matches("[^\\s@]+@[^\\s@]+\\.[^\\s@]+"))
      throw new ReglaNegocioException("El email no es válido");
    nombre = nombre.trim();
    email = email.trim().toLowerCase(java.util.Locale.ROOT);
  }

  public static Usuario nuevo(String nombre, String email) {
    return new Usuario(null, nombre, email);
  }
}
