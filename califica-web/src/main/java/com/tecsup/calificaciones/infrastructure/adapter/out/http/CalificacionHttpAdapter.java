package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.tecsup.calificaciones.domain.model.Calificacion;
import com.tecsup.calificaciones.domain.port.out.CalificacionesApiPort;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CalificacionHttpAdapter implements CalificacionesApiPort {
  private final HttpGateway http;

  public CalificacionHttpAdapter(@Value("${servicios.calificaciones.url}") String url) {
    http = new HttpGateway(url, "calificaciones");
  }

  public record Peticion(Long usuarioId, Long streamId, int puntuacion, String comentario) {}

  public Calificacion calificar(Long u, Long s, int p, String c) {
    return http.post("/api/calificaciones", new Peticion(u, s, p, c), Calificacion.class);
  }

  public List<Calificacion> listarPorStream(Long id) {
    return Arrays.asList(http.get("/api/calificaciones?streamId=" + id, Calificacion[].class));
  }
}
