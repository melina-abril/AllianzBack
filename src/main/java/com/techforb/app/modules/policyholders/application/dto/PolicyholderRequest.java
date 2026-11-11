package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PolicyholderRequest(
        @NotNull(message = "El tipo de persona es obligatorio.") PersonType personType,
        @NotBlank(message = "El nombre o razón social es obligatorio.")
        @Size(max = 160, message = "El nombre o razón social puede tener hasta 160 caracteres.") String name,
        @NotBlank(message = "El DNI o CUIT es obligatorio.") String documentNumber,
        @Size(max = 120, message = "El contacto principal puede tener hasta 120 caracteres.") String primaryContact,
        @Email(message = "Ingresá un correo electrónico válido.")
        @Size(max = 160, message = "El correo puede tener hasta 160 caracteres.") String email,
        @Size(max = 40, message = "El teléfono puede tener hasta 40 caracteres.") String phone,
        @Size(max = 240, message = "El domicilio puede tener hasta 240 caracteres.") String address) {
}
