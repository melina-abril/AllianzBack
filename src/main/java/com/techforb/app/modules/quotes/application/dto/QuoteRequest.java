package com.techforb.app.modules.quotes.application.dto;

import com.techforb.app.modules.quotes.domain.PaymentMethod;
import com.techforb.app.modules.quotes.domain.VehicleUse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/** Datos obligatorios para crear o actualizar una cotización. */
public record QuoteRequest(
        @NotBlank(message = "La marca es obligatoria.") String brand,
        @NotBlank(message = "El modelo es obligatorio.") String model,
        @NotBlank(message = "La versión es obligatoria.") String version,
        @NotNull(message = "El año es obligatorio.") @Min(value = 1900, message = "El año no es válido.") Integer year,
        @NotNull(message = "El uso del vehículo es obligatorio.") VehicleUse use,
        @NotNull(message = "Indica si el vehículo tiene GNC.") Boolean gnc,
        @NotBlank(message = "El código postal es obligatorio.")
        @Pattern(regexp = "^[0-9]{4}$", message = "El código postal debe tener cuatro dígitos.") String postalCode,
        @NotNull(message = "La fecha de nacimiento es obligatoria.") LocalDate birthDate,
        @NotNull(message = "Los años de registro son obligatorios.")
        @Min(value = 0, message = "Los años de registro no pueden ser negativos.") Integer registrationYears,
        @NotNull(message = "La cantidad de siniestros es obligatoria.")
        @Min(value = 0, message = "La cantidad de siniestros no puede ser negativa.") Integer claimsLastThreeYears,
        @NotNull(message = "El medio de pago es obligatorio.") PaymentMethod paymentMethod,
        @NotBlank(message = "El DNI es obligatorio.")
        @Size(min = 7, max = 8, message = "El DNI debe tener siete u ocho dígitos.")
        @Pattern(regexp = "^[0-9]{7,8}$", message = "El DNI debe tener siete u ocho dígitos.") String dni) {
}
