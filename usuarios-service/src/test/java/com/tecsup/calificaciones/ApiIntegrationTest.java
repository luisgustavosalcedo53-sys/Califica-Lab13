package com.tecsup.calificaciones;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ApiIntegrationTest {
  @Autowired MockMvc mvc;

  @Test
  void listaYRecursoAusente() throws Exception {
    mvc.perform(get("/api/usuarios"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists());
    mvc.perform(get("/api/usuarios/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void registroYValidacion() throws Exception {
    mvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Prueba\",\"email\":\"PRUEBA@TECSUP.EDU.PE\"}"))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.email").value("prueba@tecsup.edu.pe"));
    mvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"\",\"email\":\"invalido\"}"))
        .andExpect(status().isBadRequest());
    mvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nombre\":\"Prueba\",\"email\":\"PRUEBA@TECSUP.EDU.PE\"}"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error").value("Ya existe un usuario con ese email"));
  }
}
