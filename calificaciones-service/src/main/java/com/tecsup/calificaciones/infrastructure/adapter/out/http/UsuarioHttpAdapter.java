package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.tecsup.calificaciones.domain.port.out.UsuarioConsultaPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;

/** Consulta al servicio dueño del recurso; 404 significa que no existe. */
@Component
public class UsuarioHttpAdapter implements UsuarioConsultaPort {
  private final HttpGateway http;

  public UsuarioHttpAdapter(@Value("${servicios.usuarios.url}") String url) {
    http = new HttpGateway(url, "usuarios");
  }

  @Override
  public boolean existe(Long id) {
    try {
      http.get("/api/usuarios/" + id, Void.class);
      return true;
    } catch (HttpClientErrorException.NotFound e) {
      return false;
    }
  }
}
