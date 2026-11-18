package com.tecsup.calificaciones.infrastructure.adapter.out.persistence;

import com.tecsup.calificaciones.domain.model.Usuario;
import com.tecsup.calificaciones.domain.port.out.UsuarioRepositoryPort;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.UsuarioEntity;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository.UsuarioJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class UsuarioPersistenceAdapter implements UsuarioRepositoryPort {

  private final UsuarioJpaRepository jpa;

  public UsuarioPersistenceAdapter(UsuarioJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Usuario guardar(Usuario usuario) {
    return jpa.save(UsuarioEntity.desde(usuario)).aDominio();
  }

  @Override
  public List<Usuario> listar() {
    return jpa.findAllByOrderByNombreAsc().stream().map(UsuarioEntity::aDominio).toList();
  }

  @Override
  public Optional<Usuario> buscarPorId(Long id) {
    return jpa.findById(id).map(UsuarioEntity::aDominio);
  }

  @Override
  public boolean existePorEmail(String email) {
    return jpa.existsByEmail(email);
  }
}
