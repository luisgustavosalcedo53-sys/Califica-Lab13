package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.port.out.StreamsApiPort;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class StreamHttpAdapter implements StreamsApiPort {
  private final HttpGateway http;

  public StreamHttpAdapter(@Value("${servicios.streams.url}") String url) {
    http = new HttpGateway(url, "streams");
  }

  public record Peticion(String titulo, String categoria) {}

  public Stream crear(String titulo, String categoria) {
    return http.post("/api/streams", new Peticion(titulo, categoria), Stream.class);
  }

  public List<Stream> listar() {
    return Arrays.asList(http.get("/api/streams", Stream[].class));
  }

  public Optional<Stream> buscarPorId(Long id) {
    try {
      return Optional.ofNullable(http.get("/api/streams/" + id, Stream.class));
    } catch (HttpClientErrorException.NotFound e) {
      return Optional.empty();
    }
  }
}
