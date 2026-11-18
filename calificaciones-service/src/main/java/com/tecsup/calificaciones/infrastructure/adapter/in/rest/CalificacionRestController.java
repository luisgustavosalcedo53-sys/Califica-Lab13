package com.tecsup.calificaciones.infrastructure.adapter.in.rest;

import com.tecsup.calificaciones.domain.model.Calificacion;
import com.tecsup.calificaciones.domain.port.in.CalificacionUseCase;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/calificaciones")
public class CalificacionRestController {
  private final CalificacionUseCase service;

  public CalificacionRestController(CalificacionUseCase service) {
    this.service = service;
  }

  public record Peticion(Long usuarioId, Long streamId, int puntuacion, String comentario) {}

  @GetMapping
  public java.util.List<Calificacion> listar(@RequestParam("streamId") Long streamId) {
    return service.listarPorStream(streamId);
  }

  @PostMapping
  public ResponseEntity<Calificacion> crear(@RequestBody Peticion r) {
    Calificacion creado =
        service.calificar(r.usuarioId(), r.streamId(), r.puntuacion(), r.comentario());
    return ResponseEntity.created(URI.create("/api/calificaciones?streamId=" + creado.streamId()))
        .body(creado);
  }
}
