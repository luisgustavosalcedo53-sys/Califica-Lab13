package com.tecsup.calificaciones.infrastructure.adapter.out.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.calificaciones.domain.exception.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.*;

public class HttpGateway {
  private final RestClient client;
  private final String servicio;

  public HttpGateway(String url, String servicio) {
    var factory = new SimpleClientHttpRequestFactory();
    factory.setConnectTimeout(2000);
    factory.setReadTimeout(4000);
    this.client = RestClient.builder().baseUrl(url).requestFactory(factory).build();
    this.servicio = servicio;
  }

  public <T> T get(String path, Class<T> type) {
    return request(() -> client.get().uri(path).retrieve().body(type));
  }

  public <T> T post(String path, Object body, Class<T> type) {
    return request(() -> client.post().uri(path).body(body).retrieve().body(type));
  }

  private <T> T request(java.util.function.Supplier<T> action) {
    try {
      return action.get();
    } catch (HttpClientErrorException.NotFound e) {
      throw e;
    } catch (RestClientResponseException e) {
      if (e.getStatusCode().is5xxServerError())
        throw new ServicioNoDisponibleException(
            "El servicio de " + servicio + " no está disponible");
      String message = "La petición fue rechazada por el servicio de " + servicio;
      try {
        message =
            new ObjectMapper().readTree(e.getResponseBodyAsString()).path("error").asText(message);
      } catch (Exception ignored) {
      }
      throw new ReglaNegocioException(message);
    } catch (RestClientException e) {
      throw new ServicioNoDisponibleException("El servicio de " + servicio + " no está disponible");
    }
  }
}
