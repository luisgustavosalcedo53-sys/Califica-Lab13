package com.tecsup.calificaciones.infrastructure.adapter.out.persistence;

import com.tecsup.calificaciones.domain.model.Stream;
import com.tecsup.calificaciones.domain.port.out.StreamRepositoryPort;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity.StreamEntity;
import com.tecsup.calificaciones.infrastructure.adapter.out.persistence.repository.StreamJpaRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class StreamPersistenceAdapter implements StreamRepositoryPort {

  private final StreamJpaRepository jpa;

  public StreamPersistenceAdapter(StreamJpaRepository jpa) {
    this.jpa = jpa;
  }

  @Override
  public Stream guardar(Stream stream) {
    return jpa.save(StreamEntity.desde(stream)).aDominio();
  }

  @Override
  public List<Stream> listar() {
    return jpa.findAll().stream().map(StreamEntity::aDominio).toList();
  }

  @Override
  public Optional<Stream> buscarPorId(Long id) {
    return jpa.findById(id).map(StreamEntity::aDominio);
  }
}
