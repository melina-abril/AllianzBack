package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.Policyholder.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PolicyholderRequest(
        @NotNull(message = "El tipo de persona es obligatorio.") PersonType personType,
        @NotBlank(message = "El nombre o razón social es obligatorio.")
        @Size(max = 150, message = "El nombre puede tener hasta 150 caracteres.") String name,
        @NotBlank(message = "El documento es obligatorio.")
        @Pattern(regexp = "^[0-9 .-]{7,20}$", message = "Ingresa un DNI o CUIT válido.") String documentNumber,
        @Size(max = 120, message = "El contacto principal puede tener hasta 120 caracteres.") String primaryContact,
        @Email(message = "Ingresa un correo electrónico válido.")
        @Size(max = 150, message = "El correo puede tener hasta 150 caracteres.") String email,
        @Size(max = 40, message = "El teléfono puede tener hasta 40 caracteres.") String phone,
        @Size(max = 250, message = "El domicilio puede tener hasta 250 caracteres.") String address) {
}
