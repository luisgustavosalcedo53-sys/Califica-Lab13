package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.model.ResumenStream;
import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.port.in.StreamUseCase;
import com.tecsup.calificaciones.domain.port.out.CalificacionesApiPort;
import com.tecsup.calificaciones.domain.port.out.StreamsApiPort;
import java.util.Comparator;
import java.util.List;

/** Combina datos de dos microservicios: los streams y sus calificaciones. */
public class StreamService implements StreamUseCase {

  private final StreamsApiPort streams;
  private final CalificacionesApiPort calificaciones;

  public StreamService(StreamsApiPort streams, CalificacionesApiPort calificaciones) {
    this.streams = streams;
    this.calificaciones = calificaciones;
  }

  @Override
  public Stream crear(String titulo, String categoria) {
    return streams.crear(titulo, categoria);
  }

  /** Devuelve los streams ordenados del mejor al peor promedio. */
  @Override
  public List<ResumenStream> listarConResumen() {
    return streams.listar().stream()
        .map(this::resumir)
        .sorted(Comparator.comparingDouble(ResumenStream::promedio).reversed())
        .toList();
  }

  @Override
  public ResumenStream obtenerResumen(Long id) {
    Stream stream =
        streams.buscarPorId(id).orElseThrow(() -> new ReglaNegocioException("El stream no existe"));
    return resumir(stream);
  }

  private ResumenStream resumir(Stream stream) {
    return ResumenStream.de(stream, calificaciones.listarPorStream(stream.id()));
  }
}
