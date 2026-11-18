package com.tecsup.calificaciones.domain.model;

import java.util.List;

/** Un stream junto con su cantidad de calificaciones y su promedio. */
public record ResumenStream(Stream stream, long total, double promedio) {

  public static ResumenStream de(Stream stream, List<Calificacion> calificaciones) {
    double promedio =
        calificaciones.stream().mapToInt(Calificacion::puntuacion).average().orElse(0);
    return new ResumenStream(stream, calificaciones.size(), promedio);
  }

  public int promedioRedondeado() {
    return (int) Math.round(promedio);
  }
}
