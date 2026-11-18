package com.tecsup.reportes.service;

import com.tecsup.reportes.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.*;
import java.util.function.Supplier;

/**
 * Obtiene los datos de los tres microservicios con base de datos y arma los
 * modelos de cada reporte. Este servicio no guarda nada: solo lee y calcula.
 */
@Service
public class ReporteService {

    private final RestClient usuarios;
    private final RestClient streams;
    private final RestClient calificaciones;

    public ReporteService(RestClient.Builder builder,
                          @Value("${servicios.usuarios.url}") String usuariosUrl,
                          @Value("${servicios.streams.url}") String streamsUrl,
                          @Value("${servicios.calificaciones.url}") String calificacionesUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000);
        factory.setReadTimeout(3000);
        builder = builder.clone().requestFactory(factory);
        this.usuarios = builder.clone().baseUrl(usuariosUrl).build();
        this.streams = builder.clone().baseUrl(streamsUrl).build();
        this.calificaciones = builder.clone().baseUrl(calificacionesUrl).build();
    }

    // ------------------------------------------------------------------ reportes

    /** Cantidad de streams, de calificaciones y promedio por categoría (mejor promedio primero). */
    public List<ReporteCategoria> porCategoria() {
        List<Stream> todos = listarStreams();
        Map<Long, List<Calificacion>> cals = calificacionesPorStream(todos);

        Map<String, List<Stream>> porCategoria = new TreeMap<>();
        for (Stream s : todos) {
            porCategoria.computeIfAbsent(s.categoria(), k -> new ArrayList<>()).add(s);
        }

        List<ReporteCategoria> reporte = new ArrayList<>();
        for (Map.Entry<String, List<Stream>> e : porCategoria.entrySet()) {
            List<Calificacion> deLaCategoria = new ArrayList<>();
            for (Stream s : e.getValue()) {
                deLaCategoria.addAll(cals.get(s.id()));
            }
            reporte.add(new ReporteCategoria(e.getKey(), e.getValue().size(),
                    deLaCategoria.size(), promedio(deLaCategoria)));
        }
        reporte.sort(Comparator.comparingDouble(ReporteCategoria::promedio).reversed());
        return reporte;
    }

    /** Por cada usuario: cuántas calificaciones dio y su promedio (más activo primero). */
    public List<ResumenUsuario> porUsuario() {
        List<Usuario> todos = llamar("usuarios", () -> usuarios.get().uri("/api/usuarios")
                .retrieve().body(new ParameterizedTypeReference<List<Usuario>>() {}));
        List<Calificacion> cals = todasLasCalificaciones(listarStreams());

        List<ResumenUsuario> reporte = new ArrayList<>();
        for (Usuario u : todos) {
            List<Calificacion> delUsuario = cals.stream()
                    .filter(c -> c.usuarioId().equals(u.id()))
                    .toList();
            reporte.add(new ResumenUsuario(u.id(), u.nombre(), u.email(), delUsuario.size(), promedio(delUsuario)));
        }
        reporte.sort(Comparator.comparingInt(ResumenUsuario::calificaciones).reversed());
        return reporte;
    }

    /** Los streams que calificó un usuario, del más reciente al más antiguo. */
    public PerfilUsuario perfil(Long usuarioId) {
        Usuario usuario = buscarUsuario(usuarioId);
        List<Stream> todos = listarStreams();
        Map<Long, List<Calificacion>> cals = calificacionesPorStream(todos);

        List<PerfilUsuario.Item> items = new ArrayList<>();
        List<Calificacion> delUsuario = new ArrayList<>();
        for (Stream s : todos) {
            for (Calificacion c : cals.get(s.id())) {
                if (c.usuarioId().equals(usuarioId)) {
                    delUsuario.add(c);
                    items.add(new PerfilUsuario.Item(s.titulo(), s.categoria(),
                            c.puntuacion(), c.comentario(), c.fecha()));
                }
            }
        }
        items.sort(Comparator.comparing(PerfilUsuario.Item::fecha, Comparator.nullsLast(Comparator.reverseOrder())));
        return new PerfilUsuario(usuario, items, promedio(delUsuario));
    }

    // ------------------------------------------------------------------ llamadas HTTP

    private Usuario buscarUsuario(Long id) {
        return llamar("usuarios", () -> usuarios.get().uri("/api/usuarios/{id}", id)
                .exchange((pedido, respuesta) -> {
                    if (respuesta.getStatusCode().value() == 404)
                        throw new UsuarioNoEncontradoException();
                    if (respuesta.getStatusCode().isError())
                        throw new ServicioNoDisponibleException("usuarios");
                    return respuesta.bodyTo(Usuario.class);
                }));
    }

    private List<Stream> listarStreams() {
        return llamar("streams", () -> streams.get().uri("/api/streams")
                .retrieve().body(new ParameterizedTypeReference<List<Stream>>() {}));
    }

    private Map<Long, List<Calificacion>> calificacionesPorStream(List<Stream> lista) {
        Map<Long, List<Calificacion>> resultado = new HashMap<>();
        for (Stream s : lista) {
            List<Calificacion> cals = llamar("calificaciones", () -> calificaciones.get()
                    .uri("/api/calificaciones?streamId={id}", s.id())
                    .retrieve().body(new ParameterizedTypeReference<List<Calificacion>>() {}));
            resultado.put(s.id(), cals == null ? List.of() : cals);
        }
        return resultado;
    }

    private List<Calificacion> todasLasCalificaciones(List<Stream> lista) {
        List<Calificacion> todas = new ArrayList<>();
        calificacionesPorStream(lista).values().forEach(todas::addAll);
        return todas;
    }

    /** Ejecuta una llamada y convierte cualquier falla de red en un mensaje claro. */
    private static <T> T llamar(String servicio, Supplier<T> llamada) {
        try {
            return llamada.get();
        } catch (ResourceAccessException e) {
            throw new ServicioNoDisponibleException(servicio);
        } catch (RestClientException e) {
            throw new ServicioNoDisponibleException(servicio);
        }
    }

    private static double promedio(List<Calificacion> lista) {
        return lista.stream().mapToInt(Calificacion::puntuacion).average().orElse(0);
    }
}

