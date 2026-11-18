package com.tecsup.reportes.model;

import java.time.LocalDateTime;

/** Calificación tal como la devuelve calificaciones-service. */
public record Calificacion(Long id, Long usuarioId, Long streamId,
                           int puntuacion, String comentario, LocalDateTime fecha) {
}
