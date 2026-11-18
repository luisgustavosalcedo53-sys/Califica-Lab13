package com.tecsup.reportes.service;

/** Se lanza cuando uno de los microservicios no responde. */
public class ServicioNoDisponibleException extends RuntimeException {

    public ServicioNoDisponibleException(String servicio) {
        super("El servicio de " + servicio + " no está disponible");
    }
}
