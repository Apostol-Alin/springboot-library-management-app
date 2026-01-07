package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {
    @NotNull(message = "Book ID cannot be null")
    @Schema(description = "ID of the book being reviewed", example = "1")
    private Long book_id;

    @NotNull(message = "Client ID cannot be null")
    @Schema(description = "ID of the client writing the review", example = "1")
    private Long client_id;

    @Size(max = 511, message = "Review text must be less than 512 characters")
    @Schema(description = "Text of the review", example = "Great book, highly recommend!")
    private String review_text;

    public ReviewRequest() {}

    public ReviewRequest(Long book_id, Long client_id, Integer rating, String review_text) {
        this.book_id = book_id;
        this.client_id = client_id;
        this.review_text = review_text;
    }

    public Long getBookId() {
        return book_id;
    }

    public Long getClientId() {
        return client_id;
    }

    public String getReviewText() {
        return review_text;
    }

    public void setBookId(Long book_id) {
        this.book_id = book_id;
    }

    public void setClientId(Long client_id) {
        this.client_id = client_id;
    }

    public void setReviewText(String review_text) {
        this.review_text = review_text;
    }
}
