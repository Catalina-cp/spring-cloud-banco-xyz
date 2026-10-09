package com.bancoxyz.pagos_service.exception;

public class PagoNoEncontradoException extends RuntimeException {

    public PagoNoEncontradoException(Long id) {
        super("No se encontró el pago con ID: " + id);
    }
}