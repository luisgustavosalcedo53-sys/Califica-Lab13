package com.tecsup.calificaciones.domain.exception;

/** Se lanza cuando una operación viola una regla del negocio. */
public class ReglaNegocioException extends RuntimeException {

  public ReglaNegocioException(String mensaje) {
    super(mensaje);
  }
}
