package com.tecsup.calificaciones.infrastructure.adapter.in.web;

import com.tecsup.calificaciones.domain.exception.ServicioNoDisponibleException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class WebErrors {
  @ExceptionHandler(ServicioNoDisponibleException.class)
  @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
  public String remoto(ServicioNoDisponibleException e, Model model) {
    model.addAttribute("message", e.getMessage());
    return "error";
  }
}
