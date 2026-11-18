package com.tecsup.calificaciones;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.tecsup.calificaciones.domain.exception.ServicioNoDisponibleException;
import com.tecsup.calificaciones.domain.port.in.StreamUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WebErrorTest {
  @Autowired MockMvc mvc;
  @MockitoBean StreamUseCase streams;

  @Test
  void muestraError503() throws Exception {
    when(streams.listarConResumen())
        .thenThrow(new ServicioNoDisponibleException("El servicio de streams no está disponible"));
    mvc.perform(get("/streams"))
        .andExpect(status().isServiceUnavailable())
        .andExpect(content().string(containsString("El servicio de streams no está disponible")));
  }
}
