package com.home.finance.report.dto;

import java.time.LocalDate;

public record PeriodResponse(
        LocalDate from,
        LocalDate to
) {
}
