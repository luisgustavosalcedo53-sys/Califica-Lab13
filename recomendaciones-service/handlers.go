package main

import (
	"encoding/json"
	"errors"
	"net/http"
	"strconv"
)

func salud(w http.ResponseWriter, r *http.Request) {
	responder(w, http.StatusOK, map[string]string{"estado": "ok"})
}

// recomendar atiende GET /api/recomendaciones/{usuarioId}?limite=3
func recomendar(api *cliente) http.HandlerFunc {
	return func(w http.ResponseWriter, r *http.Request) {
		usuarioID, err := strconv.ParseInt(r.PathValue("usuarioId"), 10, 64)
		if err != nil || usuarioID < 1 {
			responder(w, http.StatusBadRequest, error_("El id del usuario debe ser un número positivo"))
			return
		}
		limite := 3
		if texto := r.URL.Query().Get("limite"); texto != "" {
			if n, err := strconv.Atoi(texto); err == nil && n > 0 && n <= 20 {
				limite = n
			}
		}

		usuario, err := api.usuario(usuarioID)
		if errors.Is(err, errNoEncontrado) {
			responder(w, http.StatusNotFound, error_("El usuario no existe"))
			return
		}
		if err != nil {
			responder(w, http.StatusServiceUnavailable, error_(err.Error()))
			return
		}

		streams, err := api.streams()
		if err != nil {
			responder(w, http.StatusServiceUnavailable, error_(err.Error()))
			return
		}
		calificaciones, err := api.calificacionesDe(streams)
		if err != nil {
			responder(w, http.StatusServiceUnavailable, error_(err.Error()))
			return
		}

		responder(w, http.StatusOK, Respuesta{
			Usuario:         usuario,
			Recomendaciones: calcularRecomendaciones(usuarioID, streams, calificaciones, limite),
		})
	}
}

func error_(mensaje string) map[string]string {
	return map[string]string{"error": mensaje}
}

func responder(w http.ResponseWriter, estado int, cuerpo any) {
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	w.WriteHeader(estado)
	json.NewEncoder(w).Encode(cuerpo)
}
