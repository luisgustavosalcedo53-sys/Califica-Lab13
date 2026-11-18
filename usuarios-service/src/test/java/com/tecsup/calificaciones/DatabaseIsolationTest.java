package com.tecsup.calificaciones;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DatabaseIsolationTest {
  @Autowired DataSource dataSource;

  @Test
  void soloContieneSuTablaYNoTieneClavesForaneas() throws Exception {
    try (var connection = dataSource.getConnection()) {
      assertTrue(connection.getMetaData().getURL().contains("usuariosdb"));
      try (var result =
          connection
              .createStatement()
              .executeQuery(
                  "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA='PUBLIC'")) {
        var tables = new ArrayList<String>();
        while (result.next()) tables.add(result.getString(1));
        assertEquals(java.util.List.of("USUARIOS"), tables);
      }
      try (var keys = connection.getMetaData().getImportedKeys(null, "PUBLIC", "USUARIOS")) {
        assertFalse(keys.next());
      }
    }
  }
}
