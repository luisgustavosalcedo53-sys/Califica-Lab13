package main

import (
	"encoding/json"
	"net/http"
	"net/http/httptest"
	"testing"
)

// Datos iguales a los data.sql de los tres microservicios.
var (
	streamsPrueba = []Stream{
		{1, "Spring Boot desde cero", "Programación"},
		{2, "Speedrun de Celeste", "Videojuegos"},
		{3, "Lofi para estudiar", "Música"},
	}
	calificacionesPrueba = map[int64][]Calificacion{
		1: {{1, 1, 1, 5, ""}, {2, 2, 1, 4, ""}},
		2: {{3, 3, 2, 5, ""}},
		3: {{4, 1, 3, 3, ""}},
	}
)

func TestNoRecomiendaLoQueYaCalifico(t *testing.T) {
	// Ana (1) ya calificó los streams 1 y 3: solo queda el 2.
	recs := calcularRecomendaciones(1, streamsPrueba, calificacionesPrueba, 3)
	if len(recs) != 1 || recs[0].StreamID != 2 {
		t.Fatalf("esperaba solo el stream 2, obtuve %+v", recs)
	}
}

func TestOrdenaPorPromedio(t *testing.T) {
	// María (3) solo calificó el 2 (Videojuegos, 5): quedan el 1 (prom 4,5) y el 3 (prom 3).
	recs := calcularRecomendaciones(3, streamsPrueba, calificacionesPrueba, 3)
	if len(recs) != 2 || recs[0].StreamID != 1 || recs[1].StreamID != 3 {
		t.Fatalf("orden inesperado: %+v", recs)
	}
}

func TestBonoPorCategoriaFavorita(t *testing.T) {
	streams := append(streamsPrueba, Stream{4, "Go para principiantes", "Programación"})
	cals := map[int64][]Calificacion{
		1: {{1, 1, 1, 5, ""}},                   // a Ana le gusta Programación
		2: {{2, 2, 2, 5, ""}, {5, 3, 2, 4, ""}}, // promedio 4,5 (Videojuegos)
		4: {{3, 2, 4, 4, ""}},                   // promedio 4 + 1 por Programación = 5
		3: {{4, 2, 3, 2, ""}},
	}
	recs := calcularRecomendaciones(1, streams, cals, 3)
	if recs[0].StreamID != 4 || recs[0].Motivo != "Te gustó otro stream de Programación" {
		t.Fatalf("esperaba primero el stream 4 por categoría favorita: %+v", recs)
	}
}

func TestRespetaElLimite(t *testing.T) {
	recs := calcularRecomendaciones(99, streamsPrueba, calificacionesPrueba, 2)
	if len(recs) != 2 {
		t.Fatalf("esperaba 2 recomendaciones, obtuve %d", len(recs))
	}
}

// Prueba de punta a punta con los tres microservicios simulados.
func TestEndpointCompleto(t *testing.T) {
	falso := httptest.NewServer(http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		switch {
		case r.URL.Path == "/api/usuarios/3":
			json.NewEncoder(w).Encode(Usuario{3, "María Huamán", "maria@tecsup.edu.pe"})
		case r.URL.Path == "/api/usuarios/9":
			w.WriteHeader(http.StatusNotFound)
		case r.URL.Path == "/api/streams":
			json.NewEncoder(w).Encode(streamsPrueba)
		case r.URL.Path == "/api/calificaciones":
			var id int64
			json.Unmarshal([]byte(r.URL.Query().Get("streamId")), &id)
			json.NewEncoder(w).Encode(calificacionesPrueba[id])
		default:
			w.WriteHeader(http.StatusNotFound)
		}
	}))
	defer falso.Close()

	api := nuevoCliente(configuracion{usuariosURL: falso.URL, streamsURL: falso.URL, calificacionesURL: falso.URL})
	mux := http.NewServeMux()
	mux.HandleFunc("GET /api/recomendaciones/{usuarioId}", recomendar(api))

	// Usuario existente
	rec := httptest.NewRecorder()
	mux.ServeHTTP(rec, httptest.NewRequest("GET", "/api/recomendaciones/3?limite=1", nil))
	var resp Respuesta
	json.NewDecoder(rec.Body).Decode(&resp)
	if rec.Code != 200 || resp.Usuario.Nombre != "María Huamán" || len(resp.Recomendaciones) != 1 ||
		resp.Recomendaciones[0].StreamID != 1 {
		t.Fatalf("respuesta inesperada: %d %+v", rec.Code, resp)
	}

	// Usuario inexistente → 404
	rec = httptest.NewRecorder()
	mux.ServeHTTP(rec, httptest.NewRequest("GET", "/api/recomendaciones/9", nil))
	if rec.Code != http.StatusNotFound {
		t.Fatalf("esperaba 404, obtuve %d", rec.Code)
	}

	// Id inválido → 400
	rec = httptest.NewRecorder()
	mux.ServeHTTP(rec, httptest.NewRequest("GET", "/api/recomendaciones/abc", nil))
	if rec.Code != http.StatusBadRequest {
		t.Fatalf("esperaba 400, obtuve %d", rec.Code)
	}

	// Servicio caído → 503
	falso.Close()
	rec = httptest.NewRecorder()
	mux.ServeHTTP(rec, httptest.NewRequest("GET", "/api/recomendaciones/3", nil))
	if rec.Code != http.StatusServiceUnavailable {
		t.Fatalf("esperaba 503, obtuve %d", rec.Code)
	}
}
