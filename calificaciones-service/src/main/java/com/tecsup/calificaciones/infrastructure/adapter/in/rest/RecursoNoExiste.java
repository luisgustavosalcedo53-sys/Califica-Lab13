package com.tecsup.calificaciones.infrastructure.adapter.in.rest;

public class RecursoNoExiste extends RuntimeException {
  public RecursoNoExiste(String m) {
    super(m);
  }
}
