package aapostol.libraryManagement.mapper;

import aapostol.libraryManagement.json.Loan;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.dto.LoanRequest;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.repository.JPABookRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoanMapper {
    @Autowired
    private JPABookRepository bookRepository;

    @Autowired
    private JPAClientRepository clientRepository;

    public Loan toEntity(LoanRequest loanRequest) {
        Loan loan = new Loan();
        Book book = bookRepository.findById(loanRequest.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + loanRequest.getBookId() + " not found."));
        Client client = clientRepository.findById(loanRequest.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + loanRequest.getClientId() + " not found."));
        loan.setBook(book);
        loan.setClient(client);
        loan.setBorrowDate(new java.util.Date());
        loan.setDueDate(loanRequest.getDueDate());
        return loan;
    }
}
