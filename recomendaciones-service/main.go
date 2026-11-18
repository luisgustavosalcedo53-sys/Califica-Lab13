// recomendaciones-service: microservicio en Go que recomienda streams a un usuario.
// No tiene base de datos: consulta por HTTP a usuarios-service, streams-service
// y calificaciones-service.
//
//	go run .
//	GET http://localhost:8085/api/recomendaciones/1?limite=3
package main

import (
	"log"
	"net/http"
	"os"
)

func main() {
	cfg := configuracion{
		puerto:            variable("PUERTO", "8085"),
		usuariosURL:       variable("USUARIOS_URL", "http://localhost:8081"),
		streamsURL:        variable("STREAMS_URL", "http://localhost:8082"),
		calificacionesURL: variable("CALIFICACIONES_URL", "http://localhost:8083"),
	}
	api := nuevoCliente(cfg)

	http.HandleFunc("GET /salud", salud)
	http.HandleFunc("GET /api/recomendaciones/{usuarioId}", recomendar(api))

	log.Printf("recomendaciones-service escuchando en :%s", cfg.puerto)
	log.Fatal(http.ListenAndServe(":"+cfg.puerto, nil))
}

type configuracion struct {
	puerto, usuariosURL, streamsURL, calificacionesURL string
}

// variable lee una variable de entorno o usa el valor por defecto.
func variable(nombre, porDefecto string) string {
	if v := os.Getenv(nombre); v != "" {
		return v
	}
	return porDefecto
}
