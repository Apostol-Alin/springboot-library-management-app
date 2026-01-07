package aapostol.libraryManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.repository.JPAReviewRepository;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {
    
    @Mock
    private JPAClientRepository clientRepository;
    
    @Mock
    private JPAReviewRepository reviewRepository;
    
    @InjectMocks
    private ClientServiceImpl clientService;
    
    private Client testClient1;
    private Client testClient2;
    private Client testClient3;
    
    @BeforeEach
    void setUp() throws Exception {
        Date date1 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(LocalDate.of(2023, 5, 20).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date3 = Date.from(LocalDate.of(2024, 3, 10).atStartOfDay(ZoneId.systemDefault()).toInstant());
        
        testClient1 = new Client(1L, "John Doe", "1234567890", date1);
        
        testClient2 = new Client(2L, "Jane Smith", "9876543210", date2);
        
        testClient3 = new Client(3L, "Bob Johnson", "5555555555", date3);
    }
    
    @Test
    void getAllClients_ShouldReturnListOfClients() {
        // Arrange
        when(clientRepository.findAll()).thenReturn(Arrays.asList(testClient1, testClient2, testClient3));
        
        // Act
        List<Client> results = clientService.getAllClients();
        
        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals("John Doe", results.get(0).getName());
        assertEquals("Jane Smith", results.get(1).getName());
        assertEquals("Bob Johnson", results.get(2).getName());
        verify(clientRepository, times(1)).findAll();
    }
    
    @Test
    void getClientsByName_ShouldReturnMatchingClients() {
        // Arrange
        when(clientRepository.findByNameContainingIgnoreCase("John"))
            .thenReturn(Arrays.asList(testClient1, testClient3));
        
        // Act
        List<Client> results = clientService.getClientsByName("John");
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().anyMatch(c -> c.getName().contains("John")));
        verify(clientRepository, times(1)).findByNameContainingIgnoreCase("John");
    }
    
    @Test
    void getClientById_ShouldReturnNull_WhenNotFound() {
        // Arrange - Edge case
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Client result = clientService.getClientById(999L);
        
        // Assert
        assertNull(result);
        verify(clientRepository, times(1)).findById(999L);
    }
    
    @Test
    void getClientByPhone_ShouldReturnClient() {
        // Arrange
        when(clientRepository.findByPhone("9876543210")).thenReturn(Optional.of(testClient2));
        
        // Act
        Client result = clientService.getClientByPhone("9876543210");
        
        // Assert
        assertNotNull(result);
        assertEquals("Jane Smith", result.getName());
        assertEquals("9876543210", result.getPhone());
        verify(clientRepository, times(1)).findByPhone("9876543210");
    }
    
    @Test
    void addClient_ShouldThrowException_WhenPhoneExists() {
        // Arrange - Edge case: duplicate phone
        Date newDate = new Date();
        Client duplicateClient = new Client("Alice Brown", "1234567890", newDate);
        when(clientRepository.findByPhone("1234567890")).thenReturn(Optional.of(testClient1));
        
        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, 
            () -> clientService.addClient(duplicateClient));
        assertTrue(exception.getMessage().contains("1234567890"));
        verify(clientRepository, never()).save(any(Client.class));
    }
    
    @Test
    void updateClientPhone_ShouldUpdateSuccessfully() {
        // Arrange
        when(clientRepository.findById(3L)).thenReturn(Optional.of(testClient3));
        when(clientRepository.save(any(Client.class))).thenReturn(testClient3);
        
        // Act
        Client result = clientService.updateClientPhone(3L, "1111111111");
        
        // Assert
        assertNotNull(result);
        assertEquals("Bob Johnson", result.getName());
        verify(clientRepository, times(1)).findById(3L);
        verify(clientRepository, times(1)).save(any(Client.class));
    }
    
    @Test
    void deleteClient_ShouldThrowException_WhenNotFound() {
        // Arrange - Edge case
        when(clientRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class, 
            () -> clientService.deleteClient(999L));
        verify(clientRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void addReviewToClient_ShouldSaveAndReturnReview() {
        // Arrange
        Review newReview = new Review();
        newReview.setClient(testClient2);
        newReview.setReviewText("Excellent book!");
        when(reviewRepository.save(newReview)).thenReturn(newReview);
        
        // Act
        Review result = clientService.addReviewToClient(newReview);
        
        // Assert
        assertNotNull(result);
        assertEquals("Excellent book!", result.getReviewText());
        verify(reviewRepository, times(1)).save(newReview);
    }
    
    @Test
    void getReviewsByClientId_ShouldReturnReviews() {
        // Arrange
        Author testAuthor = new Author(1L, "J.K. Rowling", "British author");
        Book testBook = new Book(1L, "Harry Potter", "A magical story", LocalDate.of(1997, 6, 26), 10, 5, testAuthor);
        Category testCategory = new Category(1L, "Fantasy", "Fantasy books");
        testBook.setCategories(new ArrayList<>(List.of(testCategory)));
        Author testAuthor2 = new Author(2L, "George R.R. Martin", "American novelist and screenwriter");
        Book testBook2 = new Book(2L, "A Game of Thrones", "Epic fantasy novel", LocalDate.of(1996, 8, 6), 8, 4, testAuthor2);
        testBook2.setCategories(new ArrayList<>(List.of(testCategory)));
        Review review1 = new Review(testBook, testClient2, "Loved it!");
        Review review2 = new Review(testBook2, testClient2, "Great read!");
        when(clientRepository.findById(2L)).thenReturn(Optional.of(testClient2));
        when(reviewRepository.findByClient_Id(2L)).thenReturn(Arrays.asList(review1, review2));
        
        // Act
        List<Review> results = clientService.getReviewsByClientId(2L);
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        verify(clientRepository, times(1)).findById(2L);
        verify(reviewRepository, times(1)).findByClient_Id(2L);
    }
    
    @Test
    void getReviewById_ShouldReturnOptional() {
        // Arrange - Edge case: review not found
        when(reviewRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Optional<Review> result = clientService.getReviewById(999L);
        
        // Assert
        assertTrue(result.isEmpty());
        verify(reviewRepository, times(1)).findById(999L);
    }
    
    @Test
    void deleteReview_ShouldDeleteSuccessfully() {
        // Arrange
        Review testReview = new Review();
        when(reviewRepository.findById(5L)).thenReturn(Optional.of(testReview));
        doNothing().when(reviewRepository).deleteById(5L);
        
        // Act
        clientService.deleteReview(5L);
        
        // Assert
        verify(reviewRepository, times(1)).findById(5L);
        verify(reviewRepository, times(1)).deleteById(5L);
    }
}
