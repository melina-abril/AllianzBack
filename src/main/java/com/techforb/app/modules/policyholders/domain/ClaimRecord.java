package com.techforb.app.modules.policyholders.domain;

import java.time.LocalDate;

/** Datos de siniestro asociados a la ficha de ejemplo. */
public record ClaimRecord(
        String number, ClaimStatus status, String vehicleId, String reason, LocalDate reportedDate) {
}
