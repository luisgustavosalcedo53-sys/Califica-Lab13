package main

import (
	"log"
	"net/http"
	"os"
	"time"
)

func entorno(clave, defecto string) string {
	if valor := os.Getenv(clave); valor != "" {
		return valor
	}
	return defecto
}

func main() {
	c := cliente{
		http:           &http.Client{Timeout: 3 * time.Second},
		usuarios:       entorno("USUARIOS_URL", "http://localhost:8081"),
		streams:        entorno("STREAMS_URL", "http://localhost:8082"),
		calificaciones: entorno("CALIFICACIONES_URL", "http://localhost:8083"),
	}
	servidor := &http.Server{Addr: ":8085", Handler: rutas(c), ReadHeaderTimeout: 5 * time.Second}
	log.Fatal(servidor.ListenAndServe())
}
