package main

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

func TestRecomendar(t *testing.T) {
	catalogo := []stream{{1, "Visto", "Música"}, {2, "Preferido", "Música"}, {3, "Popular", "Juegos"}}
	notas := map[int][]calificacion{1: {{3, 5}}, 2: {{4, 4}}, 3: {{4, 5}, {5, 4}}}
	resultado := recomendar(3, catalogo, notas, 1)
	if len(resultado) != 1 || resultado[0].ID != 2 || resultado[0].Promedio != 4 || resultado[0].puntaje != 5 {
		t.Fatalf("resultado inesperado: %+v", resultado)
	}
}

func TestEndpoint(t *testing.T) {
	api := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		switch r.URL.Path {
		case "/api/usuarios/3":
			_ = json.NewEncoder(w).Encode(usuario{3, "María"})
		case "/api/streams":
			_ = json.NewEncoder(w).Encode([]stream{{1, "Lofi", "Música"}})
		case "/api/calificaciones":
			if r.URL.Query().Get("streamId") != "1" {
				t.Error("falta el filtro por stream")
			}
			_ = json.NewEncoder(w).Encode([]calificacion{})
		default:
			w.WriteHeader(http.StatusNotFound)
		}
	}))
	defer api.Close()
	c := cliente{api.Client(), api.URL, api.URL, api.URL}
	for _, prueba := range []struct {
		ruta   string
		codigo int
	}{
		{"/salud", 200},
		{"/api/recomendaciones/3?limite=1", 200},
		{"/api/recomendaciones/99", 404},
		{"/api/recomendaciones/abc", 400},
		{"/api/recomendaciones/3?limite=0", 400},
	} {
		w := httptest.NewRecorder()
		rutas(c).ServeHTTP(w, httptest.NewRequest(http.MethodGet, prueba.ruta, nil))
		if w.Code != prueba.codigo || !json.Valid(w.Body.Bytes()) {
			t.Errorf("%s: estado %d y cuerpo %q", prueba.ruta, w.Code, w.Body)
		}
	}
	c.calificaciones = api.URL + "/inactivo"
	w := httptest.NewRecorder()
	rutas(c).ServeHTTP(w, httptest.NewRequest(http.MethodGet, "/api/recomendaciones/3", nil))
	if w.Code != 503 {
		t.Errorf("estado %d para dependencia caída", w.Code)
	}
}
