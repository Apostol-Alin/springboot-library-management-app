package aapostol.libraryManagement.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import aapostol.libraryManagement.json.Loan;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.repository.JPALoanRepository;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.exception.*;
import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService{
    @Autowired
    private JPALoanRepository loanRepository;

    @Autowired
    private JPABookRepository bookRepository;

    @Autowired
    private JPAClientRepository clientRepository;

    @Override
    public Loan getLoanById(Long id) {
        return loanRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Loan addLoan(Loan loan) {
        Book book = loan.getBook();
        Optional<Loan> existingLoan = loanRepository.findByClient_IdAndBook_Id(loan.getClient().getId(), book.getId());
        if (existingLoan.isPresent() && existingLoan.get().getReturnDate() == null) {
            throw new DuplicateResourceException("Client with ID " + loan.getClient().getId() + " already has an active loan for the book with ID: " + book.getId());
        }
        if (book.getAvailableCopies() <= 0) {
            throw new BusinessRuleException("No available copies for the book: " + book.getId());
        }
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        return loanRepository.save(loan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Loan updateLoanReturnDate(Long id, LocalDate returnDate) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan with ID " + id + " not found."));

        if (loan.getReturnDate() != null) {
            throw new BusinessRuleException("Loan with ID " + id + " has already been returned on " + loan.getReturnDate() + ".");
        }
        loan.setReturnDate(returnDate);

        // Increase available copies when the book is returned
        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        return loanRepository.save(loan);
    }

    @Override
    public Loan updateLoanDueDate(Long id, LocalDate dueDate) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan with ID " + id + " not found."));
        loan.setDueDate(dueDate);
        return loanRepository.save(loan);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void deleteLoan(Long id) {
        Loan loan = loanRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Loan with ID " + id + " not found."));
        // If the loan is being deleted without return date, increase available copies
        if (loan.getReturnDate() == null) {
            Book book = loan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);
        }
        loanRepository.deleteById(id);
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public List<Loan> getLoansByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + clientId + " not found."));
        return loanRepository.findByClient_Id(clientId);
    }

    @Override
    public List<Loan> getLoansByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found."));
        return loanRepository.findByBook_Id(bookId);
    }

    @Override
    public List<Loan> getLoansByClientIdOverdue(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client with ID " + clientId + " not found."));
        // Overdue loans are those with dueDate before current date and no returnDate or those who were returned late
        return loanRepository.findByClient_Id(clientId).stream()
                .filter(loan -> (loan.getDueDate().isBefore(LocalDate.now()) && loan.getReturnDate() == null) || (loan.getReturnDate() != null && loan.getDueDate().isBefore(loan.getReturnDate())))
                .toList();
    }

}
