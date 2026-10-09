package com.bancoxyz.clientes_service.exception;

public class ClienteNoEncontradoException extends RuntimeException {

    public ClienteNoEncontradoException(Long id) {
        super("No se encontró el cliente con ID: " + id);
    }
}