// package aapostol.libraryManagement.json;

// import java.util.Date;

// import org.json.JSONObject;

// import com.fasterxml.jackson.annotation.JsonProperty;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;
// import jakarta.persistence.Table;
// import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.Size;

// @Entity
// @Table(name = "Reviews")
// public class Review {
//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//     private Long id;

//     @ManyToOne
//     @JoinColumn(name = "book_id", nullable = false)
//     @NotNull(message = "Book cannot be null")
//     private Book book;

//     @ManyToOne
//     @JoinColumn(name = "ClientId", nullable = false)
//     @NotNull(message = "Client cannot be null")
//     private Client client;

//     @Column(name = "ReviewText", nullable = true, length = 511)
//     @Size(max = 511, message = "Review text must be less than 512 characters")
//     private String reviewText;

//     @Column(name = "ReviewDate", nullable = false)
//     @JsonProperty(access = JsonProperty.Access.READ_ONLY)
//     private Date reviewDate;

//     public Review() {}

//     public Review(Book book, Client client, String reviewText) {
//         this.book = book;
//         this.client = client;
//         this.reviewText = reviewText;
//         this.reviewDate = new Date();
//     }

//     public Long getId() {
//         return id;
//     }

//     public Book getBook() {
//         return book;
//     }

//     public Client getClient() {
//         return client;
//     }

//     public String getReviewText() {
//         return reviewText;
//     }

//     public Date getReviewDate() {
//         return reviewDate;
//     }


//     public void setBook(Book book) {
//         this.book = book;
//     }

//     public void setClient(Client client) {
//         this.client = client;
//     }

//     public void setReviewText(String reviewText) {
//         this.reviewText = reviewText;
//     }


//     @Override
//     public String toString() {
//         JSONObject json = new JSONObject();
//         json.put("id", id);
//         json.put("bookId", book != null ? book.getId() : null);
//         json.put("clientId", client != null ? client.getId() : null);
//         json.put("reviewText", reviewText);
//         json.put("reviewDate", reviewDate);
//         return json.toString();
//     }
// }
