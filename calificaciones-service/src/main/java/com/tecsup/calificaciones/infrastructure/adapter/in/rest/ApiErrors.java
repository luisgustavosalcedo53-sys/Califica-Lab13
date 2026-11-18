package com.tecsup.calificaciones.infrastructure.adapter.in.rest;

import com.tecsup.calificaciones.domain.exception.*;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class ApiErrors {
  @ExceptionHandler(ReglaNegocioException.class)
  ResponseEntity<?> regla(ReglaNegocioException e) {
    return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(RecursoNoExiste.class)
  ResponseEntity<?> ausente(RecursoNoExiste e) {
    return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(ServicioNoDisponibleException.class)
  ResponseEntity<?> remoto(ServicioNoDisponibleException e) {
    return ResponseEntity.status(503).body(Map.of("error", e.getMessage()));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  ResponseEntity<?> conflicto(Exception e) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "Este usuario ya calificó este stream"));
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
    org.springframework.web.bind.MissingServletRequestParameterException.class
  })
  ResponseEntity<?> formato(Exception e) {
    return ResponseEntity.badRequest()
        .body(Map.of("error", "La petición contiene datos inválidos"));
  }
}
