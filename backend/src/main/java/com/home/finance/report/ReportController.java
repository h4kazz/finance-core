package com.home.finance.report;

import com.home.finance.report.dto.ReportSummaryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/summary")
public class ReportController {
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    public ResponseEntity<ReportSummaryResponse> getUserReportSummary(
            @RequestParam(required = false) LocalDate from,
            @RequestParam(required = false) LocalDate to,
            Authentication authentication
            ) {
        String email = authentication.getName();

        LocalDate now = LocalDate.now();

        if (from == null && to == null) {
            from = now.withDayOfMonth(1);
            to = now;
        } else if (from != null && to == null) {
            to = now;
        } else if (from == null) {
            from = now.withDayOfMonth(1);
        }

        return ResponseEntity.ok(reportService.getSummary(from, to, email));
    }
}
