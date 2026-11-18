package main

import (
	"encoding/json"
	"errors"
	"net/http"
	"strconv"
)

func responder(w http.ResponseWriter, codigo int, cuerpo any) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(codigo)
	_ = json.NewEncoder(w).Encode(cuerpo)
}

func responderError(w http.ResponseWriter, err error) {
	codigo := http.StatusServiceUnavailable
	if errors.Is(err, usuarioInexistente) {
		codigo = http.StatusNotFound
	}
	responder(w, codigo, map[string]string{"error": err.Error()})
}

func rutas(c cliente) http.Handler {
	mux := http.NewServeMux()
	mux.HandleFunc("GET /salud", func(w http.ResponseWriter, r *http.Request) {
		responder(w, http.StatusOK, map[string]string{"estado": "ok"})
	})
	mux.HandleFunc("GET /api/recomendaciones/{usuarioId}", c.recomendaciones)
	return mux
}

func (c cliente) recomendaciones(w http.ResponseWriter, r *http.Request) {
	id, err := strconv.Atoi(r.PathValue("usuarioId"))
	if err != nil {
		responder(w, http.StatusBadRequest, map[string]string{"error": "El identificador debe ser un número entero"})
		return
	}
	limite := 3
	if r.URL.Query().Has("limite") {
		limite, err = strconv.Atoi(r.URL.Query().Get("limite"))
		if err != nil || limite < 1 {
			responder(w, http.StatusBadRequest, map[string]string{"error": "El límite debe ser un entero positivo"})
			return
		}
	}
	ctx := r.Context()
	var persona usuario
	if err := c.obtener(ctx, c.usuarios+"/api/usuarios/"+strconv.Itoa(id), "usuarios", &persona); err != nil {
		responderError(w, err)
		return
	}
	catalogo, err := c.catalogo(ctx)
	if err != nil {
		responderError(w, err)
		return
	}
	notas, err := c.notas(ctx, catalogo)
	if err != nil {
		responderError(w, err)
		return
	}
	responder(w, http.StatusOK, struct {
		Usuario         usuario         `json:"usuario"`
		Recomendaciones []recomendacion `json:"recomendaciones"`
	}{persona, recomendar(id, catalogo, notas, limite)})
}
