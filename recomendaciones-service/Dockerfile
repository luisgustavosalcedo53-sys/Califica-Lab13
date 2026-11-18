# Etapa 1: compilar el binario de Go (sin dependencias de C para que funcione en cualquier Linux)
FROM golang:1.22-alpine AS build
WORKDIR /app
COPY go.mod ./
COPY *.go ./
RUN CGO_ENABLED=0 go build -o recomendaciones .

# Etapa 2: imagen mínima que solo contiene el binario
FROM alpine:3.20
WORKDIR /app
COPY --from=build /app/recomendaciones .
EXPOSE 8085
ENTRYPOINT ["./recomendaciones"]
