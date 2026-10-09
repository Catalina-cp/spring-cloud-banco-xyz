package com.bancoxyz.clientes_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ClienteRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre,

        @NotBlank(message = "El RUT es obligatorio")
        @Size(max = 12, message = "El RUT no puede superar los 12 caracteres")
        String rut,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato válido")
        String email,

        @Size(max = 15, message = "El teléfono no puede superar los 15 caracteres")
        String telefono
) {}