package main

import (
	"math"
	"sort"
)

func recomendar(id int, catalogo []stream, notas map[int][]calificacion, limite int) []recomendacion {
	vistos := map[int]bool{}
	preferidas := map[string]bool{}
	for _, item := range catalogo {
		for _, nota := range notas[item.ID] {
			if nota.UsuarioID == id {
				vistos[item.ID] = true
				if nota.Puntuacion >= 4 {
					preferidas[item.Categoria] = true
				}
			}
		}
	}

	candidatos := make([]recomendacion, 0)
	for _, item := range catalogo {
		if vistos[item.ID] {
			continue
		}
		historial := notas[item.ID]
		suma := 0
		for _, nota := range historial {
			suma += nota.Puntuacion
		}
		promedio := 0.0
		motivo := "Promedio de la comunidad"
		if len(historial) > 0 {
			promedio = float64(suma) / float64(len(historial))
		} else {
			motivo = "Stream sin calificaciones"
		}
		puntaje := promedio
		if preferidas[item.Categoria] {
			puntaje++
			motivo = "Categoría valorada con 4 o 5 estrellas"
		}
		candidatos = append(candidatos, recomendacion{item, math.Round(promedio*100) / 100, motivo, puntaje})
	}
	sort.Slice(candidatos, func(i, j int) bool {
		a, b := candidatos[i], candidatos[j]
		if a.puntaje == b.puntaje {
			return a.ID < b.ID
		}
		return a.puntaje > b.puntaje
	})
	return candidatos[:min(limite, len(candidatos))]
}
