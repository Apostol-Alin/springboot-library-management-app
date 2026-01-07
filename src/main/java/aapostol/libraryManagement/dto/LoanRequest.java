package aapostol.libraryManagement.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public class LoanRequest {
    @NotNull(message = "Book ID cannot be null")
    @Schema(description = "ID of the book to be loaned", example = "1")
    private Long book_id;

    @NotNull(message = "Client ID cannot be null")
    @Schema(description = "ID of the client borrowing the book", example = "1")
    private Long client_id;

    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due date must be in the future")
    @Schema(description = "Due date for returning the book", example = "2024-12-31")
    private LocalDate due_date;

    public LoanRequest() {}

    public LoanRequest(Long book_id, Long client_id, LocalDate due_date) {
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

    public LocalDate getDueDate() {
        return due_date;
    }

    public void setBookId(Long bookId) {
        this.book_id = bookId;
    }

    public void setClientId(Long clientId) {
        this.client_id = clientId;
    }

    public void setDueDate(LocalDate dueDate) {
        this.due_date = dueDate;
    }
}
