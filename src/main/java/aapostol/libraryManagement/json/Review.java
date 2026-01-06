package aapostol.libraryManagement.json;

import java.util.Date;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book cannot be null")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @NotNull(message = "Client cannot be null")
    private Client client;

    @Column(name = "review_text", nullable = true, length = 511)
    @Size(max = 511, message = "Review text must be less than 512 characters")
    private String review_text;

    @Column(name = "review_date", nullable = false)
    @NotNull(message = "Review date cannot be null")
    private Date review_date;

    public Review() {}

    public Review(Book book, Client client, String review_text) {
        this.book = book;
        this.client = client;
        this.review_text = review_text;
        this.review_date = new Date();
    }

    public Long getId() {
        return id;
    }

    public Book getBook() {
        return book;
    }

    public Client getClient() {
        return client;
    }

    public String getReviewText() {
        return review_text;
    }

    public Date getReviewDate() {
        return review_date;
    }


    public void setBook(Book book) {
        this.book = book;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setReviewText(String review_text) {
        this.review_text = review_text;
    }

    public void setReviewDate(Date review_date) {
        this.review_date = review_date;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("bookId", book != null ? book.getId() : null);
        json.put("clientId", client != null ? client.getId() : null);
        json.put("review_text", review_text);
        json.put("review_date", review_date);
        return json.toString();
    }
}
