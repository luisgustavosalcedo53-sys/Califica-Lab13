FROM golang:1.22-alpine AS build
WORKDIR /app
COPY go.mod *.go ./
RUN CGO_ENABLED=0 go build -trimpath -ldflags="-s -w" -o /recomendaciones .

FROM alpine:3.22
COPY --from=build /recomendaciones /usr/local/bin/recomendaciones
USER 65534:65534
EXPOSE 8085
ENTRYPOINT ["/usr/local/bin/recomendaciones"]
