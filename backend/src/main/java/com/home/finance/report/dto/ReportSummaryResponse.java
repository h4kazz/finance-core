package com.home.finance.report.dto;

import java.math.BigDecimal;

public record ReportSummaryResponse(
        PeriodResponse period,
        BigDecimal totalIncome,
        BigDecimal totalExpense,
        BigDecimal totalBalance,
        long transactionCount,
        long incomeTransactionCount,
        long expenseTransactionCount
) {
}
