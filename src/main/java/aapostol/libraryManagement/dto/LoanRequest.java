package aapostol.libraryManagement.dto;

import java.util.Date;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class LoanRequest {
    @NotNull(message = "Book ID cannot be null")
    private Long book_id;

    @NotNull(message = "Client ID cannot be null")
    private Long client_id;

    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due date must be in the future")
    private Date due_date;

    public LoanRequest() {}

    public LoanRequest(Long book_id, Long client_id, Date due_date) {
        this.book_id = book_id;
        this.client_id = client_id;
        this.due_date = due_date;
    }

    public Long getBookId() {
        return book_id;
    }

    public Long getClientId() {
        return client_id;
    }

    public Date getDueDate() {
        return due_date;
    }

    public void setBookId(Long bookId) {
        this.book_id = bookId;
    }

    public void setClientId(Long clientId) {
        this.client_id = clientId;
    }

    public void setDueDate(Date dueDate) {
        this.due_date = dueDate;
    }
}
