# recomendaciones-service (Go)

Microservicio del Laboratorio 13. Recomienda a un usuario los streams que
todavía no calificó. No tiene base de datos: consulta por HTTP a
`usuarios-service`, `streams-service` y `calificaciones-service`.

Solo usa la biblioteca estándar de Go (1.22 o superior).

## Endpoints

| Método | Ruta | Respuesta |
|---|---|---|
| GET | `/salud` | `{"estado":"ok"}` |
| GET | `/api/recomendaciones/{usuarioId}?limite=3` | 200 con el usuario y sus recomendaciones |

Errores: `400` id inválido, `404` el usuario no existe, `503` algún
microservicio no responde. Todos con el cuerpo `{"error": "mensaje"}`.

Ejemplo de respuesta para `GET /api/recomendaciones/3`:

```json
{
  "usuario": { "id": 3, "nombre": "María Huamán", "email": "maria@tecsup.edu.pe" },
  "recomendaciones": [
    { "streamId": 1, "titulo": "Spring Boot desde cero", "categoria": "Programación",
      "promedio": 4.5, "calificaciones": 2, "motivo": "Bien calificado por otros usuarios" },
    { "streamId": 3, "titulo": "Lofi para estudiar", "categoria": "Música",
      "promedio": 3, "calificaciones": 1, "motivo": "Bien calificado por otros usuarios" }
  ]
}
```

## Cómo recomienda

1. Descarta los streams que el usuario ya calificó.
2. `puntaje = promedio del stream`, y suma 1 si el stream es de una categoría a
   la que el usuario le dio 4 o 5 estrellas.
3. Ordena de mayor a menor puntaje y devuelve los primeros `limite` (3 por defecto).

Las calificaciones de todos los streams se piden en paralelo, una goroutine
por stream.

## Archivos

| Archivo | Contenido |
|---|---|
| `main.go` | Configuración por variables de entorno y rutas |
| `handlers.go` | Atiende las peticiones HTTP y arma las respuestas |
| `cliente.go` | Llama a los tres microservicios (en paralelo para las calificaciones) |
| `recomendaciones.go` | El algoritmo de recomendación |
| `modelos.go` | Structs de los datos |
| `recomendaciones_test.go` | Pruebas del algoritmo y del endpoint con servicios simulados |

## Ejecutar

```bash
go test ./...      # pruebas
go run .           # usa localhost:8081, 8082 y 8083
```

Variables de entorno: `PUERTO` (8085), `USUARIOS_URL`, `STREAMS_URL`,
`CALIFICACIONES_URL`.

## Docker Compose

Copie esta carpeta dentro de `califica-microservicios` y agregue el servicio a
`docker-compose.yml`:

```yaml
  recomendaciones-service:
    build: ./recomendaciones-service
    container_name: recomendaciones-service
    ports:
      - "8085:8085"
    environment:
      USUARIOS_URL: http://usuarios-service:8081
      STREAMS_URL: http://streams-service:8082
      CALIFICACIONES_URL: http://calificaciones-service:8083
    depends_on:
      - usuarios-service
      - streams-service
      - calificaciones-service
```
