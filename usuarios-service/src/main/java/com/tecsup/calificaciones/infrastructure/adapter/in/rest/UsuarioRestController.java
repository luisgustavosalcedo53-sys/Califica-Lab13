package com.tecsup.calificaciones.infrastructure.adapter.in.rest;

import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.in.UsuarioUseCase;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioRestController {
  private final UsuarioUseCase service;

  public UsuarioRestController(UsuarioUseCase service) {
    this.service = service;
  }

  public record Peticion(String nombre, String email) {}

  @GetMapping
  public java.util.List<Usuario> listar() {
    return service.listar();
  }

  @GetMapping("/{id}")
  public Usuario buscar(@PathVariable("id") Long id) {
    return service.obtener(id);
  }

  @PostMapping
  public ResponseEntity<Usuario> crear(@RequestBody Peticion r) {
    Usuario creado = service.registrar(r.nombre(), r.email());
    return ResponseEntity.created(URI.create("/api/usuarios/" + creado.id())).body(creado);
  }
}
