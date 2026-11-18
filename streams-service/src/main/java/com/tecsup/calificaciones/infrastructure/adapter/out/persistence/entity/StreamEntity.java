package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity;

import com.tecsup.calificaciones.domain.model.Stream;
import jakarta.persistence.*;

@Entity
@Table(name = "streams")
public class StreamEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String titulo;

  @Column(nullable = false, length = 40)
  private String categoria;

  protected StreamEntity() {
    // requerido por JPA
  }

  public static StreamEntity desde(Stream stream) {
    StreamEntity e = new StreamEntity();
    e.id = stream.id();
    e.titulo = stream.titulo();
    e.categoria = stream.categoria();
    return e;
  }

  public Stream aDominio() {
    return new Stream(id, titulo, categoria);
  }
}
