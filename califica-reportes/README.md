# califica-reportes (Spring Boot MVC)

Microservicio del Laboratorio 13. Muestra reportes en páginas HTML de solo
lectura con el patrón Modelo-Vista-Controlador. No tiene base de datos:
consulta por HTTP a `usuarios-service`, `streams-service` y
`calificaciones-service`.

| Ruta | Qué muestra |
|---|---|
| `GET /reportes` | Por categoría: streams, calificaciones y promedio |
| `GET /reportes/usuarios` | Por usuario: calificaciones dadas y promedio que otorga |
| `GET /reportes/usuarios/{id}` | Perfil: los streams que calificó el usuario |

## Estructura MVC

| Paquete | Rol |
|---|---|
| `controller` | `ReporteController` (Controlador) y `ManejadorErrores` |
| `model` | Records del modelo: `ReporteCategoria`, `ResumenUsuario`, `PerfilUsuario`, y los datos de los otros servicios |
| `service` | `ReporteService`: llama a los tres microservicios con `RestClient` y arma el modelo |
| `templates` | Vistas Thymeleaf: `categorias.html`, `usuarios.html`, `perfil.html`, `error.html` |

Si un microservicio no responde, se muestra `error.html` con el mensaje
«El servicio de … no está disponible» (código 503). Si el usuario no existe,
la misma página con «El usuario no existe» (404).

## Ejecutar

```bash
mvn spring-boot:run      # http://localhost:8084, usa localhost:8081, 8082 y 8083
```

Las direcciones se configuran en `application.properties` y en Docker con
`SERVICIOS_USUARIOS_URL`, `SERVICIOS_STREAMS_URL` y `SERVICIOS_CALIFICACIONES_URL`.
