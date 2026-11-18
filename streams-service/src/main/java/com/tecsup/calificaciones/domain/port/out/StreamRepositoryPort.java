package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Stream;
import java.util.List;
import java.util.Optional;

public interface StreamRepositoryPort {
  Stream guardar(Stream stream);

  List<Stream> listar();

  Optional<Stream> buscarPorId(Long id);
}
