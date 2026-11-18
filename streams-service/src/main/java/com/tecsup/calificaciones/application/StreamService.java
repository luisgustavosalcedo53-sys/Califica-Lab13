package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.exception.NoEncontradoException;
import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.port.in.StreamUseCase;
import com.tecsup.calificaciones.domain.port.out.StreamRepositoryPort;
import java.util.List;

public class StreamService implements StreamUseCase {

  private final StreamRepositoryPort streams;

  public StreamService(StreamRepositoryPort streams) {
    this.streams = streams;
  }

  @Override
  public Stream crear(String titulo, String categoria) {
    return streams.guardar(Stream.nuevo(titulo, categoria));
  }

  @Override
  public List<Stream> listar() {
    return streams.listar();
  }

  @Override
  public Stream obtener(Long id) {
    return streams
        .buscarPorId(id)
        .orElseThrow(() -> new NoEncontradoException("El stream no existe"));
  }
}
