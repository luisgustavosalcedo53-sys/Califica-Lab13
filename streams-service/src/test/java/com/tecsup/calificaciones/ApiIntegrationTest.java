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
    mvc.perform(get("/api/streams"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].id").exists());
    mvc.perform(get("/api/streams/99999"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").exists());
  }

  @Test
  void registroYValidacion() throws Exception {
    mvc.perform(
            post("/api/streams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\"Nuevo\",\"categoria\":\"\"}"))
        .andExpect(status().isCreated())
        .andExpect(header().exists("Location"))
        .andExpect(jsonPath("$.categoria").value("General"));
    mvc.perform(
            post("/api/streams")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"titulo\":\" \"}"))
        .andExpect(status().isBadRequest());
  }
}
