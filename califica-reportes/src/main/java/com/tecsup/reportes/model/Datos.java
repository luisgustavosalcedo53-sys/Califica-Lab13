package com.tecsup.reportes.model;

import java.time.LocalDateTime;
import java.util.List;

public final class Datos {
    private Datos() {}

    public record Usuario(long id, String nombre) {}
    public record Stream(long id, String titulo, String categoria) {}
    public record Calificacion(long usuarioId, int puntuacion, String comentario, LocalDateTime fecha) {}
    public record Detalle(Stream stream, Calificacion calificacion) {}
    public record ReporteCategoria(String categoria, long streams, long calificaciones, double promedio) {}
    public record ResumenUsuario(Usuario usuario, long calificaciones, double promedio) {}
    public record PerfilUsuario(Usuario usuario, List<Detalle> historial) {}
}
