package com.tecsup.calificaciones.infrastructure.config;

import com.tecsup.calificaciones.application.*;
import com.tecsup.calificaciones.domain.port.in.*;
import com.tecsup.calificaciones.domain.port.out.*;
import org.springframework.context.annotation.*;

@Configuration
public class BeanConfig {
  @Bean
  UsuarioUseCase caso(UsuarioRepositoryPort repo) {
    return new UsuarioService(repo);
  }
}
