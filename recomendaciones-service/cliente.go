package main

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"net/http"
	"strconv"
)

var usuarioInexistente = errors.New("El usuario no existe")

type cliente struct {
	http                              *http.Client
	usuarios, streams, calificaciones string
}

func (c cliente) obtener(ctx context.Context, url, servicio string, salida any) error {
	fallo := fmt.Errorf("El servicio de %s no está disponible", servicio)
	req, err := http.NewRequestWithContext(ctx, http.MethodGet, url, nil)
	if err != nil {
		return fallo
	}
	resp, err := c.http.Do(req)
	if err != nil {
		return fallo
	}
	defer resp.Body.Close()
	if servicio == "usuarios" && resp.StatusCode == http.StatusNotFound {
		return usuarioInexistente
	}
	if resp.StatusCode != http.StatusOK || json.NewDecoder(resp.Body).Decode(salida) != nil {
		return fallo
	}
	return nil
}

func (c cliente) catalogo(ctx context.Context) ([]stream, error) {
	var items []stream
	err := c.obtener(ctx, c.streams+"/api/streams", "streams", &items)
	return items, err
}

func (c cliente) notas(ctx context.Context, catalogo []stream) (map[int][]calificacion, error) {
	type resultado struct {
		id    int
		notas []calificacion
		err   error
	}
	ctx, cancelar := context.WithCancel(ctx)
	defer cancelar()
	resultados := make(chan resultado, len(catalogo))
	for _, item := range catalogo {
		go func(id int) {
			var notas []calificacion
			err := c.obtener(ctx, c.calificaciones+"/api/calificaciones?streamId="+strconv.Itoa(id), "calificaciones", &notas)
			resultados <- resultado{id, notas, err}
		}(item.ID)
	}
	todas := make(map[int][]calificacion, len(catalogo))
	for range catalogo {
		item := <-resultados
		if item.err != nil {
			return nil, item.err
		}
		todas[item.id] = item.notas
	}
	return todas, nil
}
