package com.tecsup.calificaciones.infrastructure.config;

import com.tecsup.calificaciones.application.*;
import com.tecsup.calificaciones.domain.port.in.*;
import com.tecsup.calificaciones.domain.port.out.*;
import com.tecsup.calificaciones.infrastructure.adapter.out.http.*;
import org.springframework.context.annotation.*;

@Configuration
public class BeanConfig {
  @Bean
  UsuarioUseCase usuarios(UsuariosApiPort http) {
    return new UsuarioService(http);
  }

  @Bean
  StreamUseCase streams(StreamsApiPort streams, CalificacionesApiPort calificaciones) {
    return new StreamService(streams, calificaciones);
  }

  @Bean
  CalificacionUseCase calificaciones(CalificacionesApiPort http) {
    return new CalificacionService(http);
  }
}
