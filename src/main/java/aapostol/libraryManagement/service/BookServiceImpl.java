package aapostol.libraryManagement.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.repository.JPAAuthorRepository;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPACategoryRepository;
import aapostol.libraryManagement.repository.JPAReviewRepository;
import aapostol.libraryManagement.exception.*;;


@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private JPABookRepository bookRepository;
    @Autowired
    private JPAAuthorRepository authorRepository;
    @Autowired
    private JPACategoryRepository categoryRepository;
    @Autowired
    private JPAReviewRepository reviewRepository;

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public List<Book> getBooksByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> getBooksByAuthorName(String authorName) {
        List<Author> authors = authorRepository.findByNameContainingIgnoreCase(authorName);
        if (authors.isEmpty()) {
            return List.of();
        }
        List<Long> authorIds = authors.stream().map(Author::getId).toList();
        return bookRepository.findByAuthor_idIn(authorIds);
    }

    @Override
    public List<Book> getBooksByAuthorId(Long authorId) {
        Optional<Author> author = authorRepository.findById(authorId);
        if (!author.isPresent()){
            throw new NoSuchElementException("Author with ID " + authorId + " not found.");
        }
        return bookRepository.findByAuthor_id(authorId);
    }

    @Override
    public void deleteBookById(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            bookRepository.deleteById(id);
        } else {
            throw new NoSuchElementException("Book with ID " + id + " not found.");
        }
    }

    @Override
    public Book addBook(Book book){
        return bookRepository.save(book);
    }

    @Override
    public Book addCategoryToBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found."));
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("Category with ID " + categoryId + " not found."));
        for (Category cat : book.getCategories()) {
            if (cat.getId().equals(categoryId)) {
                throw new DuplicateResourceException("Category with ID " + categoryId + " is already associated with Book ID " + bookId + ".");
            }
        }
        book.getCategories().add(category);
        return bookRepository.save(book);
    }

    @Override
    public Book removeCategoryFromBook(Long bookId, Long categoryId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found."));
        Category category = categoryRepository.findById(categoryId)
            .orElseThrow(() -> new NoSuchElementException("Category with ID " + categoryId + " not found."));
        for (Category cat : book.getCategories()) {
            if (cat.getId().equals(categoryId)) {
                book.getCategories().remove(cat);
                return bookRepository.save(book);
            }
        }
        throw new DuplicateResourceException("Category with ID " + categoryId + " is not associated with Book ID " + bookId + ".");
    }

    @Override
    public List<Review> getReviewsByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new NoSuchElementException("Book with ID " + bookId + " not found."));
        return reviewRepository.findByBook_Id(bookId);
    }
}