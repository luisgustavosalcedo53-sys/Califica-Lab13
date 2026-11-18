package com.tecsup.reportes.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ManejadorErrores {
    @ExceptionHandler(ResponseStatusException.class)
    public ModelAndView mostrar(ResponseStatusException error) {
        var vista = new ModelAndView("error");
        vista.setStatus(error.getStatusCode());
        vista.addObject("codigo", error.getStatusCode().value());
        vista.addObject("mensaje", error.getReason());
        return vista;
    }
}
