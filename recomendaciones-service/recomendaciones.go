package main

import "sort"

// bonoCategoria se suma al puntaje de un stream cuya categoría le gusta al usuario.
const bonoCategoria = 1.0

// calcularRecomendaciones elige los streams que el usuario todavía no calificó y
// los ordena por puntaje:
//
//	puntaje = promedio del stream + 1 si es de una categoría favorita del usuario
//
// Una categoría es favorita si el usuario le puso 4 o 5 estrellas a algún stream de ella.
func calcularRecomendaciones(usuarioID int64, streams []Stream,
	calificaciones map[int64][]Calificacion, limite int) []Recomendacion {

	categoriaDe := make(map[int64]string, len(streams))
	for _, s := range streams {
		categoriaDe[s.ID] = s.Categoria
	}

	// 1. Qué streams ya calificó el usuario y qué categorías le gustan.
	yaCalifico := map[int64]bool{}
	favoritas := map[string]bool{}
	for streamID, lista := range calificaciones {
		for _, c := range lista {
			if c.UsuarioID != usuarioID {
				continue
			}
			yaCalifico[streamID] = true
			if c.Puntuacion >= 4 {
				favoritas[categoriaDe[streamID]] = true
			}
		}
	}

	// 2. Puntaje de cada stream que todavía no calificó.
	var candidatos []Recomendacion
	for _, s := range streams {
		if yaCalifico[s.ID] {
			continue
		}
		lista := calificaciones[s.ID]
		r := Recomendacion{
			StreamID:       s.ID,
			Titulo:         s.Titulo,
			Categoria:      s.Categoria,
			Promedio:       promedio(lista),
			Calificaciones: len(lista),
		}
		r.puntaje = r.Promedio
		switch {
		case favoritas[s.Categoria]:
			r.puntaje += bonoCategoria
			r.Motivo = "Te gustó otro stream de " + s.Categoria
		case len(lista) == 0:
			r.Motivo = "Nadie lo ha calificado todavía: sé el primero"
		default:
			r.Motivo = "Bien calificado por otros usuarios"
		}
		candidatos = append(candidatos, r)
	}

	// 3. Ordenar de mayor a menor puntaje; a igual puntaje, el más calificado primero.
	sort.SliceStable(candidatos, func(i, j int) bool {
		if candidatos[i].puntaje != candidatos[j].puntaje {
			return candidatos[i].puntaje > candidatos[j].puntaje
		}
		return candidatos[i].Calificaciones > candidatos[j].Calificaciones
	})
	if len(candidatos) > limite {
		candidatos = candidatos[:limite]
	}
	if candidatos == nil {
		candidatos = []Recomendacion{} // JSON [] en lugar de null
	}
	return candidatos
}

func promedio(lista []Calificacion) float64 {
	if len(lista) == 0 {
		return 0
	}
	suma := 0
	for _, c := range lista {
		suma += c.Puntuacion
	}
	return float64(suma) / float64(len(lista))
}
