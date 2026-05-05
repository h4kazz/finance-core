package com.home.finance.ReportTest;

import com.home.finance.category.model.TransactionType;
import com.home.finance.report.DefaultReportService;
import com.home.finance.report.dto.ReportSummaryResponse;
import com.home.finance.transaction.model.Transaction;
import com.home.finance.transaction.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DefaultReportServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private DefaultReportService reportService;

    @Test
    void getSummary_WhenTransactionsExist_ReturnsCalculatedSummary() {
        LocalDate from = LocalDate.of(2026, 5, 1);
        LocalDate to = LocalDate.of(2026, 5, 31);

        Transaction income = new Transaction();
        income.setTransactionType(TransactionType.INCOME);
        income.setAmount(new BigDecimal("100.00"));

        Transaction expense = new Transaction();
        expense.setTransactionType(TransactionType.EXPENSE);
        expense.setAmount(new BigDecimal("40.00"));

        when(transactionRepository.findByAccountUserEmailAndDateBetween("user@test.com", from, to))
                .thenReturn(List.of(income, expense));

        ReportSummaryResponse result = reportService.getSummary(from, to, "user@test.com");

        assertEquals(new BigDecimal("100.00"), result.totalIncome());
        assertEquals(new BigDecimal("40.00"), result.totalExpense());
        assertEquals(new BigDecimal("60.00"), result.totalBalance());
        assertEquals(2, result.transactionCount());
        assertEquals(1, result.incomeTransactionCount());
        assertEquals(1, result.expenseTransactionCount());
    }
}
