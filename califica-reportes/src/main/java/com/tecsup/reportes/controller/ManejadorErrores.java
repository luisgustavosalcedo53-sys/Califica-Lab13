package com.tecsup.reportes.controller;

import com.tecsup.reportes.service.ServicioNoDisponibleException;
import com.tecsup.reportes.service.UsuarioNoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/** Muestra la vista error.html con un mensaje claro cuando algo falla. */
@ControllerAdvice
public class ManejadorErrores {

    @ExceptionHandler(ServicioNoDisponibleException.class)
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    public String servicioNoDisponible(ServicioNoDisponibleException e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return "error";
    }

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String usuarioNoEncontrado(UsuarioNoEncontradoException e, Model model) {
        model.addAttribute("mensaje", e.getMessage());
        return "error";
    }
}
