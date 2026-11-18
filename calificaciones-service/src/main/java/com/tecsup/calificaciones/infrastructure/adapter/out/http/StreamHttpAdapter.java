package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.tecsup.calificaciones.domain.port.out.StreamConsultaPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/** Consulta al servicio dueño del recurso; 404 significa que no existe. */
@Component
public class StreamHttpAdapter implements StreamConsultaPort {
  private final HttpGateway http;

  public StreamHttpAdapter(@Value("${servicios.streams.url}") String url) {
    http = new HttpGateway(url, "streams");
  }

  @Override
  public boolean existe(Long id) {
    try {
      http.get("/api/streams/" + id, Void.class);
      return true;
    } catch (HttpClientErrorException.NotFound e) {
      return false;
    }
  }
}
