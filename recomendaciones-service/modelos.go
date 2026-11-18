package main

// Datos que llegan de los otros microservicios (mismos campos del JSON).

type Usuario struct {
	ID     int64  `json:"id"`
	Nombre string `json:"nombre"`
	Email  string `json:"email"`
}

type Stream struct {
	ID        int64  `json:"id"`
	Titulo    string `json:"titulo"`
	Categoria string `json:"categoria"`
}

type Calificacion struct {
	ID         int64  `json:"id"`
	UsuarioID  int64  `json:"usuarioId"`
	StreamID   int64  `json:"streamId"`
	Puntuacion int    `json:"puntuacion"`
	Comentario string `json:"comentario"`
}

// Datos que devuelve este microservicio.

type Recomendacion struct {
	StreamID       int64   `json:"streamId"`
	Titulo         string  `json:"titulo"`
	Categoria      string  `json:"categoria"`
	Promedio       float64 `json:"promedio"`
	Calificaciones int     `json:"calificaciones"`
	Motivo         string  `json:"motivo"`
	puntaje        float64 // solo para ordenar, no se envía
}

type Respuesta struct {
	Usuario         Usuario         `json:"usuario"`
	Recomendaciones []Recomendacion `json:"recomendaciones"`
}
