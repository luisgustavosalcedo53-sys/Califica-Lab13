# Califica · Laboratorio 13

Ampliación de Califica del laboratorio 12. Los microservicios de usuarios, streams y calificaciones conservan sus bases H2. Califica Web permite registrar notas y consultar el ranking. Los dos servicios nuevos reutilizan las API REST del proyecto.

## Ejecución

```sh
docker compose up -d --build
docker compose ps
```

| Puerto | Servicio | Ruta de entrada |
| --- | --- | --- |
| 8080 | Califica Web | /streams |
| 8081 | Usuarios | /api/usuarios |
| 8082 | Streams | /api/streams |
| 8083 | Calificaciones | /api/calificaciones?streamId=1 |
| 8084 | Reportes MVC | /reportes |
| 8085 | Recomendaciones en Go | /api/recomendaciones/3?limite=3 |

Reportes también ofrece `/reportes/usuarios` y `/reportes/usuarios/1`. La API Go responde a `/salud` y excluye el contenido ya calificado. Ordena por promedio más un punto de afinidad cuando el usuario otorgó al menos cuatro estrellas a algún stream de la categoría. En caso de empate, ordena por ID.

## Pruebas

Importar `Califica-Lab13.postman_collection.json`. Ejecutar las dos primeras solicitudes con los seis servicios activos. Para probar el 503, ejecutar `docker compose stop calificaciones-service`, enviar la tercera solicitud y recuperar el servicio con `docker compose start calificaciones-service`.

Las pruebas de Go se ejecutan con `go test ./...` desde `recomendaciones-service`.

```sh
docker images 'califica-microservicios-*'
```

