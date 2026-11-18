package com.tecsup.reportes.controller;

import com.tecsup.reportes.service.ReporteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Controlador del patrón MVC: recibe la petición, pide los datos al servicio,
 * los pone en el Model y elige la vista.
 */
@Controller
public class ReporteController {

    private final ReporteService reportes;

    public ReporteController(ReporteService reportes) {
        this.reportes = reportes;
    }

    @GetMapping("/")
    public String inicio() {
        return "redirect:/reportes";
    }

    @GetMapping("/reportes")
    public String categorias(Model model) {
        model.addAttribute("categorias", reportes.porCategoria());
        return "categorias";
    }

    @GetMapping("/reportes/usuarios")
    public String usuarios(Model model) {
        model.addAttribute("usuarios", reportes.porUsuario());
        return "usuarios";
    }

    @GetMapping("/reportes/usuarios/{id}")
    public String perfil(@PathVariable("id") Long id, Model model) {
        model.addAttribute("perfil", reportes.perfil(id));
        return "perfil";
    }
}
