package com.tecsup.reportes.controller;

import com.tecsup.reportes.service.ReporteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reportes")
public class ReporteController {
    private final ReporteService servicio;

    public ReporteController(ReporteService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public String categorias(Model model) {
        model.addAttribute("categorias", servicio.porCategoria());
        return "categorias";
    }

    @GetMapping("/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", servicio.porUsuario());
        return "usuarios";
    }

    @GetMapping("/usuarios/{id}")
    public String perfil(@PathVariable long id, Model model) {
        model.addAttribute("perfil", servicio.perfil(id));
        return "perfil";
    }
}
