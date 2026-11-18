package com.tecsup.reportes.model;

/** Una fila del reporte por categoría. */
public record ReporteCategoria(String categoria, int streams, int calificaciones, double promedio) {

    /** Promedio redondeado, para dibujar las estrellas en la vista. */
    public int estrellas() {
        return (int) Math.round(promedio);
    }
}
