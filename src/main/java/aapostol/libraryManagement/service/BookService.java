package aapostol.libraryManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Review;

@Service
public interface BookService {

    List<Book> getAllBooks();

    Book getBookById(Long id);

    List<Book> getBooksByTitle(String title);

    void deleteBookById(Long id);

    List<Book> getBooksByAuthorName(String authorName);

    List<Book> getBooksByAuthorId(Long authorId);
    
    Book addBook(Book book);

    Book addCategoryToBook(Long bookId, Long categoryId);

    Book removeCategoryFromBook(Long bookId, Long categoryId);

    List<Review> getReviewsByBookId(Long bookId);

}
