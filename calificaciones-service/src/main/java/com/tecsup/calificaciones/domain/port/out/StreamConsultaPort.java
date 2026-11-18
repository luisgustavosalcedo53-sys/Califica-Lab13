package com.tecsup.calificaciones.domain.port.out;

/**
 * Puerto de salida hacia el microservicio de streams. En el monolito era StreamRepositoryPort;
 * ahora los streams viven en otro servicio.
 */
public interface StreamConsultaPort {
  boolean existe(Long streamId);
}
