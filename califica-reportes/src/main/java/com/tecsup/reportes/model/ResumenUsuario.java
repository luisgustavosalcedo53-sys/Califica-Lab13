package com.tecsup.reportes.model;

/** Una fila del reporte de usuarios: cuánto califica cada uno y qué nota pone en promedio. */
public record ResumenUsuario(Long id, String nombre, String email, int calificaciones, double promedio) {
}
