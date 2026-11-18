package com.tecsup.calificaciones.infrastructure.adapter.out.persistence.entity;

import com.tecsup.calificaciones.domain.model.Usuario;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 80)
  private String nombre;

  @Column(nullable = false, unique = true, length = 120)
  private String email;

  protected UsuarioEntity() {
    // requerido por JPA
  }

  public static UsuarioEntity desde(Usuario usuario) {
    UsuarioEntity e = new UsuarioEntity();
    e.id = usuario.id();
    e.nombre = usuario.nombre();
    e.email = usuario.email();
    return e;
  }

  public Usuario aDominio() {
    return new Usuario(id, nombre, email);
  }
}
