package com.tecsup.calificaciones.infrastructure.adapter.in.web;

import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.in.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UsuarioController {

    private final UsuarioUseCase usuarios;

    public UsuarioController(UsuarioUseCase usuarios) {
        this.usuarios = usuarios;
    }

    @GetMapping("/usuarios")
    public String listar(Model model) {
        model.addAttribute("usuarios", usuarios.listar());
        return "usuarios";
    }

    @PostMapping("/usuarios")
    public String registrar(@RequestParam("nombre") String nombre,
                            @RequestParam("email") String email,
                            RedirectAttributes flash) {
        try {
            Usuario nuevo = usuarios.registrar(nombre, email);
            flash.addFlashAttribute("ok", "Usuario registrado: " + nuevo.nombre());
        } catch (ReglaNegocioException e) {
            flash.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }
}
