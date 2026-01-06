package aapostol.libraryManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Loan;

public interface JPALoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByClient_Id(Long clientId);
    List<Loan> findByBook_Id(Long bookId);
    Optional<Loan> findByClient_IdAndBook_Id(Long clientId, Long bookId);
}
