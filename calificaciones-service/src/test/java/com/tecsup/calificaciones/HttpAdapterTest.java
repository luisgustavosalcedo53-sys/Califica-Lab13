package com.tecsup.calificaciones;

import static org.junit.jupiter.api.Assertions.*;

import com.sun.net.httpserver.HttpServer;
import com.tecsup.calificaciones.domain.exception.*;
import com.tecsup.calificaciones.infrastructure.adapter.out.http.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

class HttpAdapterTest {
  @Test
  void consultaHttpY404() throws Exception {
    HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext(
        "/api/usuarios/1",
        e -> {
          byte[] b =
              "{\"id\":1,\"nombre\":\"Ana\",\"email\":\"ana@tecsup.edu.pe\"}"
                  .getBytes(StandardCharsets.UTF_8);
          e.getResponseHeaders().set("Content-Type", "application/json");
          e.sendResponseHeaders(200, b.length);
          e.getResponseBody().write(b);
          e.close();
        });
    server.createContext(
        "/api/usuarios/99",
        e -> {
          e.sendResponseHeaders(404, -1);
          e.close();
        });
    server.createContext(
        "/api/usuarios/500",
        e -> {
          e.sendResponseHeaders(500, -1);
          e.close();
        });
    server.start();
    var adapter = new UsuarioHttpAdapter("http://127.0.0.1:" + server.getAddress().getPort());
    try {
      assertTrue(adapter.existe(1L));
      assertFalse(adapter.existe(99L));
      assertThrows(ServicioNoDisponibleException.class, () -> adapter.existe(500L));
    } finally {
      server.stop(0);
    }
    assertThrows(ServicioNoDisponibleException.class, () -> adapter.existe(1L));
  }
}
