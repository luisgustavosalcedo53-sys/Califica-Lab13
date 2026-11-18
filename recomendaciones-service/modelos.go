package main

type usuario struct {
	ID     int    `json:"id"`
	Nombre string `json:"nombre"`
}

type stream struct {
	ID        int    `json:"id"`
	Titulo    string `json:"titulo"`
	Categoria string `json:"categoria"`
}

type calificacion struct {
	UsuarioID  int `json:"usuarioId"`
	Puntuacion int `json:"puntuacion"`
}

type recomendacion struct {
	stream
	Promedio float64 `json:"promedio"`
	Motivo   string  `json:"motivo"`
	puntaje  float64
}
