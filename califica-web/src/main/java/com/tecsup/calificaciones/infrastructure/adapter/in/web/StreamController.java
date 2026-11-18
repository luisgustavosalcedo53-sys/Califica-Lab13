package com.tecsup.calificaciones.infrastructure.adapter.in.web;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.in.CalificacionUseCase;
import com.tecsup.calificaciones.domain.port.in.StreamUseCase;
import com.tecsup.calificaciones.domain.port.in.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class StreamController {

    private final StreamUseCase streams;
    private final CalificacionUseCase calificaciones;
    private final UsuarioUseCase usuarios;

    public StreamController(StreamUseCase streams,
                            CalificacionUseCase calificaciones,
                            UsuarioUseCase usuarios) {
        this.streams = streams;
        this.calificaciones = calificaciones;
        this.usuarios = usuarios;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/streams";
    }

    @GetMapping("/streams")
    public String listar(Model model) {
        model.addAttribute("resumenes", streams.listarConResumen());
        return "streams";
    }

    @PostMapping("/streams")
    public String crear(@RequestParam("titulo") String titulo,
                        @RequestParam(name = "categoria", defaultValue = "") String categoria,
                        RedirectAttributes flash) {
        try {
            Stream creado = streams.crear(titulo, categoria);
            flash.addFlashAttribute("ok", "Stream creado: " + creado.titulo());
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/streams";
    }

    @GetMapping("/streams/{id}")
    public String detalle(@PathVariable("id") Long id, Model model, RedirectAttributes flash) {
        try {
            model.addAttribute("resumen", streams.obtenerResumen(id));
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
            return "redirect:/streams";
        }
        List<Usuario> lista = usuarios.listar();
        Map<Long, String> nombres = lista.stream()
                .collect(Collectors.toMap(Usuario::id, Usuario::nombre));

        model.addAttribute("usuarios", lista);
        model.addAttribute("nombres", nombres);
        model.addAttribute("calificaciones", calificaciones.listarPorStream(id));
        return "stream-detalle";
    }
}
