package com.tecsup.calificaciones.domain.port.in;

import com.tecsup.calificaciones.domain.model.Stream;
import java.util.List;

public interface StreamUseCase {
  Stream crear(String titulo, String categoria);

  List<Stream> listar();

  Stream obtener(Long id);
}
