package aapostol.libraryManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Loan;
import java.time.LocalDate;

@Service
public interface LoanService {
    public Loan getLoanById(Long id);
    public Loan addLoan(Loan loan);
    public Loan updateLoanReturnDate(Long id, LocalDate returnDate);
    public Loan updateLoanDueDate(Long id, LocalDate dueDate);
    public void deleteLoan(Long id);
    public List<Loan> getAllLoans();
    public List<Loan> getLoansByClientId(Long clientId);
    public List<Loan> getLoansByBookId(Long bookId);
    public List<Loan> getLoansByClientIdOverdue(Long clientId);
}
