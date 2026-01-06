package aapostol.libraryManagement.dto;

import java.util.Date;

import org.json.JSONObject;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class BookRequest {

    @NotBlank(message = "Book title cannot be blank")
    @NotNull(message = "Book title cannot be null")
    @Size(max = 255, message = "Book title must be less than 256 characters")
    private String title;

    @Size(max = 511, message = "Book description must be less than 512 characters")
    private String description;

    @PastOrPresent(message = "Publication date cannot be in the future")
    private Date publication_date;

    @Positive(message = "Total copies must be a positive integer")
    private Integer total_copies;

    @Positive(message = "Available copies must be a positive integer")
    private Integer available_copies;

    @AssertTrue(message = "Available copies must not exceed total copies")
    public boolean isavailable_copiesValid() {
        return available_copies <= total_copies;
    }

    @NotNull(message = "Author cannot be null")
    private Long author_id;

    public BookRequest(){}
    public BookRequest(String title, String description, Date publication_date, Integer total_copies, Integer available_copies, Long author_id) {
        this.title = title;
        this.description = description;
        this.publication_date = publication_date;
        this.total_copies = total_copies;
        this.available_copies = available_copies;
        this.author_id = author_id;
    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public Date getPublicationDate() {
        return publication_date;
    }
    public Integer getTotalCopies() {
        return total_copies;
    }
    public Integer getAvailableCopies() {
        return available_copies;
    }
    public Long getAuthorId() {
        return author_id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPublicationDate(Date publication_date) {
        this.publication_date = publication_date;
    }
    public void setTotalCopies(Integer total_copies) {
        this.total_copies = total_copies;
    }
    public void setAvailableCopies(Integer available_copies) {
        this.available_copies = available_copies;
    }
    public void setAuthorId(Long author_id) {
        this.author_id = author_id;
    }
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("title", title);
        json.put("description", description);
        json.put("publication_date", publication_date);
        json.put("total_copies", total_copies);
        json.put("available_copies", available_copies);
        json.put("author_id", author_id);
        return json.toString();
    }
}