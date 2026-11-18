package com.tecsup.reportes.service;

import static com.tecsup.reportes.model.Datos.*;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ReporteService {
    private final RestClient usuarios;
    private final RestClient streams;
    private final RestClient calificaciones;

    public ReporteService(@Value("${servicios.usuarios.url}") String usuariosUrl,
                          @Value("${servicios.streams.url}") String streamsUrl,
                          @Value("${servicios.calificaciones.url}") String calificacionesUrl) {
        usuarios = cliente(usuariosUrl);
        streams = cliente(streamsUrl);
        calificaciones = cliente(calificacionesUrl);
    }

    private static RestClient cliente(String url) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(2));
        factory.setReadTimeout(Duration.ofSeconds(3));
        return RestClient.builder().baseUrl(url).requestFactory(factory).build();
    }

    private static <T> List<T> lista(RestClient cliente, String servicio, String ruta, Class<T[]> tipo) {
        try {
            return Arrays.asList(Objects.requireNonNull(
                cliente.get().uri(ruta).retrieve().body(tipo)));
        } catch (RestClientException | NullPointerException error) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                "El servicio de " + servicio + " no está disponible", error);
        }
    }

    private List<Stream> streams() {
        return lista(streams, "streams", "/api/streams", Stream[].class);
    }

    private List<Usuario> usuarios() {
        return lista(usuarios, "usuarios", "/api/usuarios", Usuario[].class);
    }

    private List<Detalle> historial(List<Stream> catalogo) {
        return catalogo.stream().flatMap(stream -> lista(calificaciones, "calificaciones",
            "/api/calificaciones?streamId=" + stream.id(), Calificacion[].class)
            .stream().map(nota -> new Detalle(stream, nota))).toList();
    }

    private static DoubleSummaryStatistics resumen(List<Detalle> historial) {
        return historial.stream().mapToDouble(d -> d.calificacion().puntuacion()).summaryStatistics();
    }

    public List<ReporteCategoria> porCategoria() {
        var catalogo = streams();
        var notas = historial(catalogo).stream().collect(Collectors.groupingBy(d -> d.stream().categoria()));
        var grupos = catalogo.stream().collect(Collectors.groupingBy(Stream::categoria, TreeMap::new, Collectors.counting()));
        return grupos.entrySet().stream().map(grupo -> {
            var cifras = resumen(notas.getOrDefault(grupo.getKey(), List.of()));
            return new ReporteCategoria(grupo.getKey(), grupo.getValue(), cifras.getCount(), cifras.getAverage());
        }).toList();
    }

    public List<ResumenUsuario> porUsuario() {
        var personas = usuarios();
        var notas = historial(streams()).stream().collect(Collectors.groupingBy(d -> d.calificacion().usuarioId()));
        return personas.stream().map(persona -> {
            var cifras = resumen(notas.getOrDefault(persona.id(), List.of()));
            return new ResumenUsuario(persona, cifras.getCount(), cifras.getAverage());
        }).toList();
    }

    public PerfilUsuario perfil(long id) {
        var usuario = usuarios().stream().filter(persona -> persona.id() == id).findFirst()
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe"));
        var notas = historial(streams()).stream()
            .filter(d -> d.calificacion().usuarioId() == id)
            .sorted(Comparator.comparing((Detalle d) -> d.calificacion().fecha()).reversed()).toList();
        return new PerfilUsuario(usuario, notas);
    }
}
