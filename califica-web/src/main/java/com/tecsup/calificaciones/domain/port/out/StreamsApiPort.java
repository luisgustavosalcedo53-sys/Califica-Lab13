package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Stream;
import java.util.List;
import java.util.Optional;

/** Puerto de salida hacia streams-service (antes: StreamRepositoryPort con JPA). */
public interface StreamsApiPort {
  Stream crear(String titulo, String categoria);

  List<Stream> listar();

  Optional<Stream> buscarPorId(Long id);
}
