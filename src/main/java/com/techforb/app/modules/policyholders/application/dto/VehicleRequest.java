package com.techforb.app.modules.policyholders.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank(message = "La patente es obligatoria.")
        @Pattern(regexp = "^[A-Za-z0-9 -]{6,12}$", message = "Ingresa una patente válida.") String plate,
        @NotBlank(message = "La marca es obligatoria.")
        @Size(max = 80, message = "La marca puede tener hasta 80 caracteres.") String brand,
        @NotBlank(message = "El modelo es obligatorio.")
        @Size(max = 80, message = "El modelo puede tener hasta 80 caracteres.") String model,
        @Min(value = 1900, message = "El año debe ser posterior a 1899.")
        @Max(value = 2100, message = "El año no es válido.") int year,
        @NotBlank(message = "El número de motor es obligatorio.") String engineNumber,
        @NotBlank(message = "El número de chasis es obligatorio.") String chassisNumber) {
}
