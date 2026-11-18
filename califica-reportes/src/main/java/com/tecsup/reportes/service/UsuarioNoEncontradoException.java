package com.tecsup.reportes.service;

/** Se lanza cuando usuarios-service responde 404. */
public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException() {
        super("El usuario no existe");
    }
}
