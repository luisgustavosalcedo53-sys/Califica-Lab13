package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.tecsup.calificaciones.domain.model.Usuario;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UsuarioHttpAdapter
    implements com.tecsup.calificaciones.domain.port.out.UsuariosApiPort {
  private final HttpGateway http;

  public UsuarioHttpAdapter(@Value("${servicios.usuarios.url}") String url) {
    http = new HttpGateway(url, "usuarios");
  }

  public record Registro(String nombre, String email) {}

  public Usuario registrar(String nombre, String email) {
    return http.post("/api/usuarios", new Registro(nombre, email), Usuario.class);
  }

  public List<Usuario> listar() {
    return Arrays.asList(http.get("/api/usuarios", Usuario[].class));
  }
}
