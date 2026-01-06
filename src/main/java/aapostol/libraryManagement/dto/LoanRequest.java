package aapostol.libraryManagement.dto;

import java.util.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class LoanRequest {
    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due date must be in the future")
    private Date dueDate;

    public LoanRequest() {}

    public LoanRequest(Long bookId, Long clientId, Date dueDate) {
        this.bookId = bookId;
        this.clientId = clientId;
        this.dueDate = dueDate;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}
