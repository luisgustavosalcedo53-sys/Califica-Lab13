package main

import (
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
	"sync"
	"time"
)

// errNoEncontrado indica que el recurso pedido no existe (404 en el otro servicio).
var errNoEncontrado = errors.New("no encontrado")

// errServicio indica que otro microservicio no respondió o respondió con error.
type errServicio struct{ servicio string }

func (e errServicio) Error() string {
	return "El servicio de " + e.servicio + " no está disponible"
}

// cliente llama a los tres microservicios que tienen base de datos H2.
type cliente struct {
	cfg  configuracion
	http *http.Client
}

func nuevoCliente(cfg configuracion) *cliente {
	return &cliente{cfg: cfg, http: &http.Client{Timeout: 5 * time.Second}}
}

func (c *cliente) usuario(id int64) (Usuario, error) {
	var u Usuario
	err := c.get("usuarios", c.cfg.usuariosURL+"/api/usuarios/"+strconv.FormatInt(id, 10), &u)
	return u, err
}

func (c *cliente) streams() ([]Stream, error) {
	var lista []Stream
	err := c.get("streams", c.cfg.streamsURL+"/api/streams", &lista)
	return lista, err
}

// calificacionesDe pide las calificaciones de todos los streams al mismo tiempo,
// una goroutine por stream, y las devuelve agrupadas por id de stream.
func (c *cliente) calificacionesDe(streams []Stream) (map[int64][]Calificacion, error) {
	var (
		mu        sync.Mutex
		wg        sync.WaitGroup
		resultado = make(map[int64][]Calificacion, len(streams))
		primerErr error
	)
	for _, s := range streams {
		wg.Add(1)
		go func(streamID int64) {
			defer wg.Done()
			var lista []Calificacion
			url := fmt.Sprintf("%s/api/calificaciones?streamId=%d", c.cfg.calificacionesURL, streamID)
			err := c.get("calificaciones", url, &lista)

			mu.Lock()
			defer mu.Unlock()
			if err != nil && primerErr == nil {
				primerErr = err
			}
			resultado[streamID] = lista
		}(s.ID)
	}
	wg.Wait()
	return resultado, primerErr
}

// get hace la petición y decodifica el JSON en destino.
func (c *cliente) get(servicio, url string, destino any) error {
	resp, err := c.http.Get(url)
	if err != nil {
		return errServicio{servicio}
	}
	defer resp.Body.Close()

	switch {
	case resp.StatusCode == http.StatusNotFound:
		return errNoEncontrado
	case resp.StatusCode != http.StatusOK:
		return errServicio{servicio}
	}
	if err := json.NewDecoder(resp.Body).Decode(destino); err != nil {
		return errServicio{servicio}
	}
	return nil
}
