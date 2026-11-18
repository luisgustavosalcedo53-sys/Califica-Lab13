package com.tecsup.reportes.model;

import java.time.LocalDateTime;
import java.util.List;

/** Perfil de un usuario: los streams que calificó. */
public record PerfilUsuario(Usuario usuario, List<Item> items, double promedio) {

    /** Una calificación del usuario, con los datos del stream. */
    public record Item(String titulo, String categoria, int puntuacion, String comentario, LocalDateTime fecha) {
    }
}
