package com.techforb.app.modules.policyholders.application.dto;

import com.techforb.app.modules.policyholders.domain.DocumentType;
import com.techforb.app.modules.policyholders.domain.PersonType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PolicyholderRequest(
        @NotNull(message = "El tipo de persona es obligatorio.") PersonType personType,
        @NotBlank(message = "El nombre o razón social es obligatorio.")
        @Size(max = 150, message = "El nombre o razón social puede tener hasta 150 caracteres.") String name,
        @NotNull(message = "El tipo de documento es obligatorio.") DocumentType documentType,
        @NotBlank(message = "El documento es obligatorio.")
        @Pattern(regexp = "[0-9 .-]+", message = "El documento solo admite números y separadores.") String documentNumber,
        @Size(max = 120, message = "El contacto principal puede tener hasta 120 caracteres.") String primaryContact,
        @Email(message = "Ingresá un correo electrónico válido.")
        @Size(max = 150, message = "El correo electrónico puede tener hasta 150 caracteres.") String email,
        @Size(max = 40, message = "El teléfono puede tener hasta 40 caracteres.") String phone,
        @Size(max = 200, message = "El domicilio puede tener hasta 200 caracteres.") String address) {
}
