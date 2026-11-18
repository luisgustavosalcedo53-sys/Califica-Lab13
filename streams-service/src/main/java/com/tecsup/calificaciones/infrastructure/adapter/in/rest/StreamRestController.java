package com.tecsup.calificaciones.infrastructure.adapter.in.rest;

import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.port.in.StreamUseCase;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/streams")
public class StreamRestController {
  private final StreamUseCase service;

  public StreamRestController(StreamUseCase service) {
    this.service = service;
  }

  public record Peticion(String titulo, String categoria) {}

  @GetMapping
  public java.util.List<Stream> listar() {
    return service.listar();
  }

  @GetMapping("/{id}")
  public Stream buscar(@PathVariable("id") Long id) {
    return service.obtener(id);
  }

  @PostMapping
  public ResponseEntity<Stream> crear(@RequestBody Peticion r) {
    Stream creado = service.crear(r.titulo(), r.categoria());
    return ResponseEntity.created(URI.create("/api/streams/" + creado.id())).body(creado);
  }
}
