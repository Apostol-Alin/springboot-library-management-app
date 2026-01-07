package aapostol.libraryManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.repository.JPAAuthorRepository;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPACategoryRepository;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @Mock
    private JPABookRepository bookRepository;
    
    @Mock
    private JPACategoryRepository categoryRepository;

    @Mock 
    private JPAAuthorRepository authorRepository;
    
    @InjectMocks
    private BookServiceImpl bookService;
    
    private Book testBook;
    private Author testAuthor;
    private Category testCategory;
    private Book testBook2;
    private Author testAuthor2;
    private Category testCategory2;
    
    @BeforeEach
    void setUp() {
        testAuthor = new Author(1L, "J.K. Rowling", "British author");
        testBook = new Book(1L, "Harry Potter", "A magical story", LocalDate.of(1997, 6, 26), 10, 5, testAuthor);
        testCategory = new Category(1L, "Fantasy", "Fantasy books");
        testBook.setCategories(new ArrayList<>(List.of(testCategory)));
        testAuthor2 = new Author(2L, "George R.R. Martin", "American novelist and screenwriter");
        testBook2 = new Book(2L, "A Game of Thrones", "Epic fantasy novel", LocalDate.of(1996, 8, 6), 8, 4, testAuthor2);
        testBook2.setCategories(new ArrayList<>(List.of(testCategory)));
        testCategory2 = new Category(2L, "Adventure", "Adventure books");
    }
    
    @Test
    void getAllBooks_ShouldReturnListOfBooks() {
        // Arrange
        List<Book> expectedBooks = Arrays.asList(testBook);
        when(bookRepository.findAll()).thenReturn(expectedBooks);
        
        // Act
        List<Book> actualBooks = bookService.getAllBooks();
        
        // Assert
        assertNotNull(actualBooks);
        assertEquals(1, actualBooks.size());
        verify(bookRepository, times(1)).findAll();
    }
    
    @Test
    void getBookById_ShouldReturnNull_WhenBookDoesNotExist() {
        // Arrange - Edge case: non-existent book
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Book result = bookService.getBookById(999L);
        
        // Assert
        assertNull(result);
        verify(bookRepository, times(1)).findById(999L);
    }
    
    @Test
    void getBooksByTitle_ShouldReturnMatchingBooks() {
        // Arrange
        when(bookRepository.findByTitleContainingIgnoreCase("Potter")).thenReturn(Arrays.asList(testBook));
        
        // Act
        List<Book> results = bookService.getBooksByTitle("Potter");
        
        // Assert
        assertEquals(1, results.size());
        assertEquals("Harry Potter", results.get(0).getTitle());
        verify(bookRepository, times(1)).findByTitleContainingIgnoreCase("Potter");
    }

    @Test
    void deleteBookById_ShouldThrowException_WhenNotFound() {
        // Arrange - Edge case
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class, 
            () -> bookService.deleteBookById(999L));
        verify(bookRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void addBook_ShouldSaveAndReturnBook() {
        // Arrange
        when(bookRepository.save(testBook)).thenReturn(testBook);
        
        // Act
        Book savedBook = bookService.addBook(testBook);
        
        // Assert
        assertNotNull(savedBook);
        assertEquals("Harry Potter", savedBook.getTitle());
        verify(bookRepository, times(1)).save(testBook);
    }
    
    @Test
    void addCategoryToBook_ShouldAddCategorySuccessfully() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(testCategory2));
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        
        // Act
        Book result = bookService.addCategoryToBook(1L, 2L);
        
        // Assert
        assertNotNull(result);
        assertTrue(result.getCategories().contains(testCategory2));
        assertTrue(result.getCategories().contains(testCategory)); // verify existing category still present
        verify(bookRepository, times(1)).save(result);
    }
    
    @Test
    void getBooksByAuthorId_ShouldReturnBooksForAuthor() {
        // Arrange
        when(bookRepository.findByAuthor_id(1L)).thenReturn(Arrays.asList(testBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(testAuthor));
        
        // Act
        List<Book> results = bookService.getBooksByAuthorId(1L);
        
        // Assert
        assertEquals(1, results.size());
        verify(bookRepository, times(1)).findByAuthor_id(1L);
        verify(authorRepository, times(1)).findById(1L);
    }
}
