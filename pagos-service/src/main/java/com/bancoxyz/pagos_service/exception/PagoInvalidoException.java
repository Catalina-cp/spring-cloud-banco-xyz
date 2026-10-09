package com.bancoxyz.pagos_service.exception;

public class PagoInvalidoException extends RuntimeException {

    public PagoInvalidoException(String mensaje) {
        super(mensaje);
    }
}