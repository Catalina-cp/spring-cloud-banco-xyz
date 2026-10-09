package com.bancoxyz.clientes_service.exception;

public class ClienteDuplicadoException extends RuntimeException {

    public ClienteDuplicadoException(String rut) {
        super("Ya existe un cliente con el RUT: " + rut);
    }
}