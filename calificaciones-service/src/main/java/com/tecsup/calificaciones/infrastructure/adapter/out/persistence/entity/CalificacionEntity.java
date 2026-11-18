package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity;

import com.tecsup.calificaciones.domain.model.Calificacion;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "calificaciones",
    uniqueConstraints = @UniqueConstraint(columnNames = {"usuario_id", "stream_id"}))
public class CalificacionEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "usuario_id", nullable = false)
  private Long usuarioId;

  @Column(name = "stream_id", nullable = false)
  private Long streamId;

  @Column(nullable = false)
  private int puntuacion;

  @Column(length = 280)
  private String comentario;

  @Column(nullable = false)
  private LocalDateTime fecha;

  protected CalificacionEntity() {
    // requerido por JPA
  }

  public static CalificacionEntity desde(Calificacion c) {
    CalificacionEntity e = new CalificacionEntity();
    e.id = c.id();
    e.usuarioId = c.usuarioId();
    e.streamId = c.streamId();
    e.puntuacion = c.puntuacion();
    e.comentario = c.comentario();
    e.fecha = c.fecha();
    return e;
  }

  public Calificacion aDominio() {
    return new Calificacion(id, usuarioId, streamId, puntuacion, comentario, fecha);
  }
}
