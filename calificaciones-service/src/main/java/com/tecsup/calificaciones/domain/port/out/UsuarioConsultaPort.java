package com.tecsup.calificaciones.domain.port.out;

/**
 * Puerto de salida hacia el microservicio de usuarios. En el monolito era UsuarioRepositoryPort;
 * ahora los usuarios viven en otro servicio.
 */
public interface UsuarioConsultaPort {
  boolean existe(Long usuarioId);
}
