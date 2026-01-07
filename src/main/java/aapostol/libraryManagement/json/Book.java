package aapostol.libraryManagement.json;

import java.time.LocalDate;
import java.util.Set;

import org.json.JSONObject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the book", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "Title", nullable = false, length = 255, unique = true)
    @NotBlank(message = "Book title cannot be blank")
    @NotNull(message = "Book title cannot be null")
    @Size(max = 255, message = "Book title must be less than 256 characters")
    @Schema(description = "Title of the book", example = "Harry Potter and the Philosopher's Stone")
    private String title;

    @Column(name = "Description", nullable = true, length = 511)
    @Size(max = 511, message = "Book description must be less than 512 characters")
    @Schema(description = "Description of the book", example = "A young wizard's journey begins.")
    private String description;

    @Column(name = "publication_date", nullable = true)
    @PastOrPresent(message = "Publication date cannot be in the future")
    @Schema(description = "Publication date of the book. Format must be yyyy-MM-dd", example = "1997-06-26")
    private LocalDate publication_date;

    @Column(name = "total_copies", nullable = false)
    @Positive(message = "Total copies must be a positive integer")
    @Schema(description = "Total number of copies of the book in the library.", example = "10")
    private Integer total_copies;

    @Column(name = "available_copies", nullable = false)
    @Positive(message = "Available copies must be a positive integer")
    @Schema(description = "Number of copies of the book currently available in the library.", example = "7")
    private Integer available_copies;

    @AssertTrue(message = "Available copies must not exceed total copies")
    @Schema(hidden = true)
    public boolean isavailable_copiesValid() {
        return available_copies <= total_copies;
    }

    @ManyToOne
    @JoinColumn(name = "AuthorId", nullable = false)
    @NotNull(message = "Author cannot be null")
    @Schema(description = "Author of the book")
    private Author author;

    @ManyToMany
    @JoinTable(name = "Book_Categories",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id"))
    @Schema(description = "Categories associated with this Book")
    private Set<Category> categories;

    public Book(){}
    public Book(Long id, String title, String description, LocalDate publication_date, Integer total_copies, Integer available_copies, Author author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.publication_date = publication_date;
        this.total_copies = total_copies;
        this.available_copies = available_copies;
        this.author = author;
    }
    public Long getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public LocalDate getPublicationDate() {
        return publication_date;
    }
    public Integer getTotalCopies() {
        return total_copies;
    }
    public Integer getAvailableCopies() {
        return available_copies;
    }
    public Author getAuthor() {
        return author;
    }
    public Set<Category> getCategories(){
        return categories;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setPublicationDate(LocalDate publication_date) {
        this.publication_date = publication_date;
    }
    public void setTotalCopies(Integer total_copies) {
        this.total_copies = total_copies;
    }
    public void setAvailableCopies(Integer available_copies) {
        this.available_copies = available_copies;
    }
    public void setAuthor(Author author) {
        this.author = author;
    }
    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("title", title);
        json.put("description", description);
        json.put("publication_date", publication_date);
        json.put("total_copies", total_copies);
        json.put("available_copies", available_copies);
        json.put("author_id", author.getId());
        return json.toString();
    }
}