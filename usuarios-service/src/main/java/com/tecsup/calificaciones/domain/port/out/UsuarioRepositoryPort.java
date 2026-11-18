package com.tecsup.calificaciones.domain.port.out;

import com.tecsup.calificaciones.domain.model.Usuario;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepositoryPort {
  Usuario guardar(Usuario usuario);

  List<Usuario> listar();

  Optional<Usuario> buscarPorId(Long id);

  boolean existePorEmail(String email);
}
