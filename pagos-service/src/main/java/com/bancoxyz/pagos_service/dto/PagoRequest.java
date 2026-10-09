package com.bancoxyz.pagos_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PagoRequest(

        @NotNull(message = "La cuenta de origen es obligatoria")
        Long cuentaOrigenId,

        Long cuentaDestinoId,

        @NotBlank(message = "El tipo es obligatorio")
        @Pattern(regexp = "PAGO|TRANSFERENCIA|DEPOSITO", message = "El tipo debe ser PAGO, TRANSFERENCIA o DEPOSITO")
        String tipo,

        @NotNull(message = "El monto es obligatorio")
        @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
        BigDecimal monto
) {}