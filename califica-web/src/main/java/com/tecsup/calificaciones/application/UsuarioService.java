package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.in.UsuarioUseCase;
import com.tecsup.calificaciones.domain.port.out.UsuariosApiPort;
import java.util.List;

/** Delegado simple: la regla del email único vive en usuarios-service. */
public class UsuarioService implements UsuarioUseCase {

  private final UsuariosApiPort usuarios;

  public UsuarioService(UsuariosApiPort usuarios) {
    this.usuarios = usuarios;
  }

  @Override
  public Usuario registrar(String nombre, String email) {
    return usuarios.registrar(nombre, email);
  }

  @Override
  public List<Usuario> listar() {
    return usuarios.listar();
  }
}
