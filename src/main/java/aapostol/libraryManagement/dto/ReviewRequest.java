package aapostol.libraryManagement.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ReviewRequest {
    @NotNull(message = "Book ID cannot be null")
    private Long bookId;

    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotNull(message = "Rating cannot be null")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private Integer rating;

    @Size(max = 511, message = "Review text must be less than 512 characters")
    private String reviewText;

    public ReviewRequest() {}

    public ReviewRequest(Long bookId, Long clientId, Integer rating, String reviewText) {
        this.bookId = bookId;
        this.clientId = clientId;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    public Long getBookId() {
        return bookId;
    }

    public Long getClientId() {
        return clientId;
    }

    public Integer getRating() {
        return rating;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }
}
