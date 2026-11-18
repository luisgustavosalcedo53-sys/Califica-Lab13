package com.tecsup.calificaciones;

import static org.junit.jupiter.api.Assertions.*;

import com.tecsup.calificaciones.application.CalificacionService;
import com.tecsup.calificaciones.domain.exception.*;
import com.tecsup.calificaciones.domain.model.*;
import com.tecsup.calificaciones.domain.port.out.*;
import java.util.*;
import org.junit.jupiter.api.Test;

class ReglasCalificacionTest {
  static class Repo implements CalificacionRepositoryPort {
    List<Calificacion> datos = new ArrayList<>();

    public Calificacion guardar(Calificacion c) {
      datos.add(c);
      return c;
    }

    public List<Calificacion> listarPorStream(Long id) {
      return datos;
    }

    public boolean existe(Long u, Long s) {
      return datos.stream().anyMatch(c -> c.usuarioId().equals(u) && c.streamId().equals(s));
    }
  }

  CalificacionService servicio() {
    return new CalificacionService(new Repo(), id -> id.equals(1L), id -> id.equals(1L));
  }

  @Test
  void puntuacionYComentario() {
    var s = servicio();
    assertThrows(ReglaNegocioException.class, () -> s.calificar(1L, 1L, 0, ""));
    assertThrows(ReglaNegocioException.class, () -> s.calificar(1L, 1L, 6, ""));
    assertThrows(ReglaNegocioException.class, () -> s.calificar(1L, 1L, 5, "x".repeat(281)));
    assertEquals(5, s.calificar(1L, 1L, 5, "Bien").puntuacion());
  }

  @Test
  void referenciasYDuplicado() {
    var s = servicio();
    assertThrows(ReglaNegocioException.class, () -> s.calificar(99L, 1L, 3, ""));
    assertThrows(ReglaNegocioException.class, () -> s.calificar(1L, 99L, 3, ""));
    s.calificar(1L, 1L, 4, "");
    assertEquals(
        "Este usuario ya calificó este stream",
        assertThrows(ReglaNegocioException.class, () -> s.calificar(1L, 1L, 2, "")).getMessage());
  }
}
