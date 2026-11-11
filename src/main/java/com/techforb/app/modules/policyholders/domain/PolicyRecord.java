package com.techforb.app.modules.policyholders.domain;

import java.time.LocalDate;
import java.util.List;

/** Datos de póliza asociados a la ficha de ejemplo. */
public record PolicyRecord(
        String number, PolicyStatus status, String coverageDescription, List<String> vehicleIds,
        LocalDate startDate, LocalDate endDate) {

    public boolean isActiveOn(LocalDate date) {
        return status == PolicyStatus.ACTIVE && !date.isBefore(startDate) && !date.isAfter(endDate);
    }
}
