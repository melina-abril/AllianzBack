package com.techforb.app.modules.policyholders.application.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record VehicleRequest(
        @NotBlank(message = "El asegurado es obligatorio.") String policyholderId,
        @NotBlank(message = "La patente es obligatoria.") String plate,
        @NotBlank(message = "La marca es obligatoria.")
        @Size(max = 80, message = "La marca puede tener hasta 80 caracteres.") String make,
        @NotBlank(message = "El modelo es obligatorio.")
        @Size(max = 80, message = "El modelo puede tener hasta 80 caracteres.") String model,
        @NotNull(message = "El año es obligatorio.")
        @Min(value = 1900, message = "El año no puede ser anterior a 1900.")
        @Max(value = 2100, message = "El año no puede ser posterior a 2100.") Integer year,
        @NotBlank(message = "El número de motor es obligatorio.")
        @Size(max = 60, message = "El número de motor puede tener hasta 60 caracteres.") String engineNumber,
        @NotBlank(message = "El número de chasis es obligatorio.")
        @Size(max = 60, message = "El número de chasis puede tener hasta 60 caracteres.") String chassisNumber) {
}
