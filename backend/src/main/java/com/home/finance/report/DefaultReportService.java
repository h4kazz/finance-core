package com.home.finance.report;

import com.home.finance.account.model.Account;
import com.home.finance.account.repository.AccountRepository;
import com.home.finance.category.model.TransactionType;
import com.home.finance.report.dto.PeriodResponse;
import com.home.finance.report.dto.ReportSummaryResponse;
import com.home.finance.transaction.model.Transaction;
import com.home.finance.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class DefaultReportService implements ReportService {
    private final TransactionRepository transactionRepository;

    public DefaultReportService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public ReportSummaryResponse getSummary(LocalDate from, LocalDate to, String email) {
        List<Transaction> transactions = transactionRepository.findByAccountUserEmailAndDateBetween(email, from, to);
        PeriodResponse period = new PeriodResponse(from, to);

        BigDecimal totalIncome = calculateTotalByType(transactions, TransactionType.INCOME);
        BigDecimal totalExpense = calculateTotalByType(transactions, TransactionType.EXPENSE);
        BigDecimal totalBalance = totalIncome.subtract(totalExpense);

        return new ReportSummaryResponse(
                period,
                totalIncome,
                totalExpense,
                totalBalance,
                transactions.size(),
                countTransactions(transactions, TransactionType.INCOME),
                countTransactions(transactions, TransactionType.EXPENSE)
        );
    }

    private BigDecimal calculateTotalByType(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == type)
                .map(Transaction::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    private long countTransactions(List<Transaction> transactions, TransactionType type) {
        return transactions.stream()
                .filter(t -> t.getTransactionType() == type)
                .count();
    }
}
