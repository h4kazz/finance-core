package com.home.finance.report;

import com.home.finance.report.dto.ReportSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ReportService {

    ReportSummaryResponse getSummary(LocalDate from, LocalDate to, String email);

}
