package com.tecsup.calificaciones.domain.port.in;

import com.tecsup.calificaciones.domain.model.Usuario;

import java.util.List;

public interface UsuarioUseCase {
    Usuario registrar(String nombre, String email);
    List<Usuario> listar();
}
