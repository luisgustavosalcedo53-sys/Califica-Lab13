package com.tecsup.calificaciones.domain.exception;

/** Se lanza cuando el stream pedido no existe. La API responde 404. */
public class NoEncontradoException extends ReglaNegocioException {

  public NoEncontradoException(String mensaje) {
    super(mensaje);
  }
}
