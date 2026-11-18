package com.tecsup.calificaciones.domain.model;

import java.time.LocalDateTime;

public record Calificacion(
    Long id,
    Long usuarioId,
    Long streamId,
    int puntuacion,
    String comentario,
    LocalDateTime fecha) {}
