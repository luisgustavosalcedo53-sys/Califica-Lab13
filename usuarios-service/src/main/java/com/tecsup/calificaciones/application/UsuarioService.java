package com.tecsup.calificaciones.application;

import com.tecsup.calificaciones.domain.exception.NoEncontradoException;
import com.tecsup.calificaciones.domain.exception.ReglaNegocioException;
import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.in.UsuarioUseCase;
import com.tecsup.calificaciones.domain.port.out.UsuarioRepositoryPort;
import java.util.List;

public class UsuarioService implements UsuarioUseCase {

  private final UsuarioRepositoryPort usuarios;

  public UsuarioService(UsuarioRepositoryPort usuarios) {
    this.usuarios = usuarios;
  }

  @Override
  public Usuario registrar(String nombre, String email) {
    Usuario nuevo = Usuario.nuevo(nombre, email);
    if (usuarios.existePorEmail(nuevo.email()))
      throw new ReglaNegocioException("Ya existe un usuario con ese email");
    return usuarios.guardar(nuevo);
  }

  @Override
  public List<Usuario> listar() {
    return usuarios.listar();
  }

  @Override
  public Usuario obtener(Long id) {
    return usuarios
        .buscarPorId(id)
        .orElseThrow(() -> new NoEncontradoException("El usuario no existe"));
  }
}
