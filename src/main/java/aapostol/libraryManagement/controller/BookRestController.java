package aapostol.libraryManagement.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aapostol.libraryManagement.dto.BookRequest;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.mapper.BookMapper;
import aapostol.libraryManagement.service.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/book-management/books")
@Tag(name = "Book Management", description = "API endpoints for managing books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @GetMapping
    @Operation(description = "Retrieve a list of all books in the system. If no books are found, an empty list is returned.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> bookList = this.bookService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/id")
    @Operation(summary = "Get Book by ID", description = "Retrieve a book by its unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the book")
    @ApiResponse(responseCode = "404", description = "Book not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Book> getBookById(@RequestParam(value = "id") Long id) {
        Book book = this.bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @DeleteMapping(value = "/id")
    @Operation(summary = "Delete Book by ID", description = "Delete a book by its unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the book")
    @ApiResponse(responseCode = "404", description = "Book not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Void> deleteBookById(@RequestParam(value = "id") Long id) {
        this.bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/title")
    @Operation(summary = "Get Books by Title", description = "Retrieve a list of books matching the given title case-insensitively and allowing partial matches")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books matching the title")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam(value = "title") String title) {
        List<Book> bookList = this.bookService.getBooksByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/author-name")
    @Operation(summary = "Get Books by Author Name", description = "Retrieve a list of books matching the given author name case-insensitively and allowing partial matches")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books matching the author name")
    public ResponseEntity<List<Book>> getBooksByAuthorName(@RequestParam(value = "author-name") String authorName) {
        List<Book> bookList = this.bookService.getBooksByAuthorName(authorName);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/author-id")
    @Operation(summary = "Get Books by Author ID", description = "Retrieve a list of books written by the author with the specified ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books by the author ID")
    @ApiResponse(responseCode = "404", description = "Author not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<List<Book>> getBooksByAuthorId(@RequestParam(value = "author-id") Long authorId) {
        List<Book> bookList = this.bookService.getBooksByAuthorId(authorId);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new Book", description = "Add a new book to the library")
    @ApiResponse(responseCode = "201", description = "Successfully added the book")
    public ResponseEntity<Book> addBook(@RequestBody @Valid BookRequest bookRequest) {
        Book bookToAdd = bookMapper.toEntity(bookRequest);
        Book addedBook = this.bookService.addBook(bookToAdd);
        return ResponseEntity.created(URI.create("/id?id=" + addedBook.getId())).body(addedBook);
    }

    @PatchMapping(value = "/add-category")
    @Operation(summary = "Add Category to Book", description = "Associate an existing category with an existing book")
    @ApiResponse(responseCode = "200", description = "Successfully added the category to the book")
    @ApiResponse(responseCode = "404", description = "Book or Category not found with the provided IDs", content = @Content(schema = @Schema(implementation = Void.class)))
    @ApiResponse(responseCode = "409", description = "The category is already associated with the book", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Book> addCategoryToBook(@RequestParam(value = "book-id") Long bookId, @RequestParam(value = "category-id") Long categoryId) {
        Book updatedBook = this.bookService.addCategoryToBook(bookId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    @PatchMapping(value = "/remove-category")
    @Operation(summary = "Remove Category from Book", description = "Disassociate an existing category from an existing book")
    @ApiResponse(responseCode = "200", description = "Successfully removed the category from the book")
    @ApiResponse(responseCode = "404", description = "Book or Category not found with the provided IDs or the category is not associated with the book", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Book> removeCategoryFromBook(@RequestParam(value = "book-id") Long bookId, @RequestParam(value = "category-id") Long categoryId) {
        Book updatedBook = this.bookService.removeCategoryFromBook(bookId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    @GetMapping(value = "/reviews")
    @Operation(summary = "Get Reviews by Book ID", description = "Retrieve a list of reviews for the book with the specified ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of reviews for the book")
    @ApiResponse(responseCode = "404", description = "Book not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<List<Review>> getReviewsByBookId(@RequestParam(value = "book-id") Long id) {
        List<Review> reviews = this.bookService.getReviewsByBookId(id);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}