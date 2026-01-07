package aapostol.libraryManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import aapostol.libraryManagement.exception.DuplicateResourceException;
import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.repository.JPAAuthorRepository;

@ExtendWith(MockitoExtension.class)
public class AuthorServiceTest {
    
    @Mock
    private JPAAuthorRepository authorRepository;
    
    @InjectMocks
    private AuthorServiceImpl authorService;
    
    private Author testAuthor1;
    private Author testAuthor2;
    
    @BeforeEach
    void setUp() {
        testAuthor1 = new Author(1L, "J.K. Rowling", "British author of Harry Potter series");
        testAuthor2 = new Author(2L, "George R.R. Martin", "American novelist and screenwriter");
    }
    
    @Test
    void getAllAuthors_ShouldReturnListOfAuthors_WhenAuthorsExist() {
        // Arrange - Set up test data and mock behavior
        List<Author> expectedAuthors = Arrays.asList(testAuthor1, testAuthor2);
        when(authorRepository.findAll()).thenReturn(expectedAuthors);
        
        // Act - Execute the method being tested
        List<Author> actualAuthors = authorService.getAllAuthors();
        
        // Assert - Verify the results
        assertNotNull(actualAuthors);
        assertEquals(2, actualAuthors.size());
        assertEquals(expectedAuthors, actualAuthors);
        verify(authorRepository, times(1)).findAll();
    }
    
    @Test
    void searchAuthorsByName_ShouldBeCaseInsensitive() {
        // Arrange
        String searchName = "ROWLING";
        List<Author> expectedAuthors = Collections.singletonList(testAuthor1);
        when(authorRepository.findByNameContainingIgnoreCase(searchName)).thenReturn(expectedAuthors);
        
        // Act
        List<Author> actualAuthors = authorService.searchAuthorsByName(searchName);
        
        // Assert
        assertNotNull(actualAuthors);
        assertEquals(1, actualAuthors.size());
        verify(authorRepository, times(1)).findByNameContainingIgnoreCase(searchName);
    }
    
    @Test
    void getAuthorById_ShouldReturnNull_WhenAuthorDoesNotExist() {
        // Arrange
        Long authorId = 999L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());
        
        // Act
        Author actualAuthor = authorService.getAuthorById(authorId);
        
        // Assert
        assertNull(actualAuthor);
        verify(authorRepository, times(1)).findById(authorId);
    }
    
    @Test
    void addAuthor_ShouldSaveAndReturnAuthor_WhenAuthorIsNew() {
        // Arrange
        Author newAuthor = new Author(null, "Stephen King", "American author of horror fiction");
        when(authorRepository.findByNameIgnoreCase(newAuthor.getName())).thenReturn(Collections.emptyList());
        when(authorRepository.save(newAuthor)).thenReturn(new Author(3L, newAuthor.getName(), newAuthor.getBiography()));
        
        // Act
        Author savedAuthor = authorService.addAuthor(newAuthor);
        
        // Assert
        assertNotNull(savedAuthor);
        assertEquals(3L, savedAuthor.getId());
        assertEquals(newAuthor.getName(), savedAuthor.getName());
        assertEquals(newAuthor.getBiography(), savedAuthor.getBiography());
        verify(authorRepository, times(1)).findByNameIgnoreCase(newAuthor.getName());
        verify(authorRepository, times(1)).save(newAuthor);
    }

    @Test
    void updateAuthorBiography_ShouldUpdateAndReturnAuthor_WhenAuthorExists() {
        // Arrange
        Long authorId = 1L;
        String newBiography = "Updated biography for J.K. Rowling";
        Author updatedAuthor = new Author(1L, "J.K. Rowling", newBiography);
        
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(testAuthor1));
        when(authorRepository.save(any(Author.class))).thenReturn(updatedAuthor);
        
        // Act
        Author result = authorService.updateAuthorBiography(authorId, newBiography);
        
        // Assert
        assertNotNull(result);
        assertEquals(newBiography, result.getBiography());
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    void deleteAuthorById_ShouldDeleteAuthor_WhenAuthorExists() {
        // Arrange
        Long authorId = 1L;
        when(authorRepository.findById(authorId)).thenReturn(Optional.of(testAuthor1));
        doNothing().when(authorRepository).deleteById(authorId);
        
        // Act
        authorService.deleteAuthorById(authorId);
        
        // Assert
        verify(authorRepository, times(1)).findById(authorId);
        verify(authorRepository, times(1)).deleteById(authorId);
    }
    
}
