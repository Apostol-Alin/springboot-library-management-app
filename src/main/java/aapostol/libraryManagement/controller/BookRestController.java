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
import aapostol.libraryManagement.mapper.BookMapper;
import aapostol.libraryManagement.service.BookService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/book-management/books")
public class BookRestController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper bookMapper;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> bookList = this.bookService.getAllBooks();
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/id")
    public ResponseEntity<Book> getBookById(@RequestParam(value = "id") Long id) {
        Book book = this.bookService.getBookById(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteBookById(@RequestParam(value = "id") Long id) {
        this.bookService.deleteBookById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/title")
    public ResponseEntity<List<Book>> getBooksByTitle(@RequestParam(value = "title") String title) {
        List<Book> bookList = this.bookService.getBooksByTitle(title);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/author-name")
    public ResponseEntity<List<Book>> getBooksByAuthorName(@RequestParam(value = "author-name") String authorName) {
        List<Book> bookList = this.bookService.getBooksByAuthorName(authorName);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @GetMapping(value = "/author-id")
    public ResponseEntity<List<Book>> getBooksByAuthorId(@RequestParam(value = "author-id") Long authorId) {
        List<Book> bookList = this.bookService.getBooksByAuthorId(authorId);
        return ResponseEntity.status(HttpStatus.OK).body(bookList);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Book> addBook(@RequestBody @Valid BookRequest bookRequest) {
        Book bookToAdd = bookMapper.toEntity(bookRequest);
        Book addedBook = this.bookService.addBook(bookToAdd);
        return ResponseEntity.created(URI.create("/id?id=" + addedBook.getId())).body(addedBook);
    }

    @PatchMapping(value = "/add-category")
    public ResponseEntity<Book> addCategoryToBook(@RequestParam(value = "book-id") Long bookId, @RequestParam(value = "category-id") Long categoryId) {
        Book updatedBook = this.bookService.addCategoryToBook(bookId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }

    @PatchMapping(value = "/remove-category")
    public ResponseEntity<Book> removeCategoryFromBook(@RequestParam(value = "book-id") Long bookId, @RequestParam(value = "category-id") Long categoryId) {
        Book updatedBook = this.bookService.removeCategoryFromBook(bookId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBook);
    }
}