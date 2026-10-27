package com.techforb.app.modules.policyholders.domain;

import java.time.LocalDate;

/** Datos de siniestros asociados a la ficha de ejemplo; sustituir por registros reales. */
public record ClaimRecord(String number, ClaimStatus status, String vehicleId,
        String reason, LocalDate reportedDate) {
}
