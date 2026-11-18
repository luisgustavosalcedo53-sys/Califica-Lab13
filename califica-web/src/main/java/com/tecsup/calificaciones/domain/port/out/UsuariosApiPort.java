package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Usuario;
import java.util.List;

/** Puerto de salida hacia usuarios-service (antes: UsuarioRepositoryPort con JPA). */
public interface UsuariosApiPort {
  Usuario registrar(String nombre, String email);

  List<Usuario> listar();
}
