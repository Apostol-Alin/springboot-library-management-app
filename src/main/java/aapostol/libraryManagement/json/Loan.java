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
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Loans")
public class Loan {
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

    @Column(name = "borrow_date", nullable = false)
    @NotNull(message = "Borrow date cannot be null")
    private Date borrow_date;

    @Column(name = "due_date", nullable = false)
    @NotNull(message = "Due date cannot be null")
    @Future(message = "Due date must be in the future")
    private Date due_date;

    @Column(name = "return_date")
    private Date return_date;

    public Loan() {}

    public Loan(Book book, Client client, Date borrow_date, Date return_date, Date due_date) {
        this.book = book;
        this.client = client;
        this.borrow_date = borrow_date;
        this.return_date = return_date;
        this.due_date = due_date;
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

    public Date getBorrowDate() {
        return borrow_date;
    }

    public Date getDueDate() {
        return due_date;
    }

    public Date getReturnDate() {
        return return_date;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrow_date = borrowDate;
    }

    public void setDueDate(Date dueDate) {
        this.due_date = dueDate;
    }

    public void setReturnDate(Date returnDate) {
        this.return_date = returnDate;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("bookId", book != null ? book.getId() : null);
        json.put("clientId", client != null ? client.getId() : null);
        json.put("borrow_date", borrow_date);
        json.put("due_date", due_date);
        json.put("return_date", return_date);
        return json.toString();
    }
}
