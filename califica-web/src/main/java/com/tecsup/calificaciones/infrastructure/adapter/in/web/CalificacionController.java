package com.tecsup.calificaciones.infrastructure.adapter.in.web;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.port.in.CalificacionUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CalificacionController {

    private final CalificacionUseCase calificaciones;

    public CalificacionController(CalificacionUseCase calificaciones) {
        this.calificaciones = calificaciones;
    }

    @PostMapping("/streams/{id}/calificaciones")
    public String calificar(@PathVariable("id") Long streamId,
                            @RequestParam(name = "usuarioId", required = false) Long usuarioId,
                            @RequestParam(name = "puntuacion", defaultValue = "0") int puntuacion,
                            @RequestParam(name = "comentario", defaultValue = "") String comentario,
                            RedirectAttributes flash) {
        try {
            calificaciones.calificar(usuarioId, streamId, puntuacion, comentario);
            flash.addFlashAttribute("ok", "Calificación guardada");
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/streams/" + streamId;
    }
}
