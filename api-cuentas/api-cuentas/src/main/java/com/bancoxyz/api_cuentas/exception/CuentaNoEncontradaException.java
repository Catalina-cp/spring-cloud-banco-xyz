package com.bancoxyz.api_cuentas.exception;

public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException(Long cuentaId) {
        super("No se encontró la cuenta con ID: " + cuentaId);
    }
}