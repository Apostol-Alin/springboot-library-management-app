package aapostol.libraryManagement.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
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

import aapostol.libraryManagement.exception.BusinessRuleException;
import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Loan;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.repository.JPALoanRepository;

@ExtendWith(MockitoExtension.class)
public class LoanServiceTest {
    
    @Mock
    private JPALoanRepository loanRepository;
    
    @Mock
    private JPABookRepository bookRepository;
    
    @Mock
    private JPAClientRepository clientRepository;
    
    @InjectMocks
    private LoanServiceImpl loanService;
    
    private Loan testLoan1;
    private Loan testLoan2;
    private Loan testLoan3;
    private Book testBook1;
    private Book testBook2;
    private Client testClient1;
    private Client testClient2;
    
    @BeforeEach
    void setUp() throws Exception {
        // Create authors
        Author author1 = new Author(1L, "Test Author 1", "Bio 1");
        Author author2 = new Author(2L, "Test Author 2", "Bio 2");
        
        // Create books
        testBook1 = new Book(1L, "Test Book 1", "Description 1", LocalDate.of(2020, 1, 1), 5, 3, author1);
        testBook2 = new Book(2L, "Test Book 2", "Description 2", LocalDate.of(2021, 6, 15), 10, 8, author2);
        
        // Create clients
        Date date1 = Date.from(LocalDate.of(2023, 1, 15).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date date2 = Date.from(LocalDate.of(2023, 5, 20).atStartOfDay(ZoneId.systemDefault()).toInstant());
        testClient1 = new Client("John Doe", "1234567890", date1);
        setClientId(testClient1, 1L);
        testClient2 = new Client("Jane Smith", "9876543210", date2);
        setClientId(testClient2, 2L);
        
        // Create loans
        testLoan1 = new Loan(testBook1, testClient1, LocalDate.now(), null, LocalDate.now().plusDays(14));
        setLoanId(testLoan1, 1L);
        
        testLoan2 = new Loan(testBook2, testClient1, LocalDate.now().minusDays(20), null, LocalDate.now().minusDays(5));
        setLoanId(testLoan2, 2L);
        
        testLoan3 = new Loan(testBook1, testClient2, LocalDate.now().minusDays(10), LocalDate.now().minusDays(3), LocalDate.now().plusDays(4));
        setLoanId(testLoan3, 3L);
    }
    
    private void setLoanId(Loan loan, Long id) throws Exception {
        Field idField = Loan.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(loan, id);
    }
    
    private void setClientId(Client client, Long id) throws Exception {
        Field idField = Client.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(client, id);
    }
    
    @Test
    void getLoanById_ShouldReturnLoan_WhenExists() {
        // Arrange
        when(loanRepository.findById(2L)).thenReturn(Optional.of(testLoan2));
        
        // Act
        Loan result = loanService.getLoanById(2L);
        
        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getId());
        assertEquals(testBook2.getId(), result.getBook().getId());
        assertEquals(testClient1.getId(), result.getClient().getId());
        verify(loanRepository, times(1)).findById(2L);
    }
    
    @Test
    void addLoan_ShouldThrowException_WhenNoAvailableCopies() {
        // Arrange - Edge case: no available copies
        testBook1.setAvailableCopies(0);
        Loan newLoan = new Loan(testBook1, testClient2, LocalDate.now(), null, LocalDate.now().plusDays(14));
        when(loanRepository.findByClient_IdAndBook_Id(testClient2.getId(), testBook1.getId()))
            .thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(BusinessRuleException.class, 
            () -> loanService.addLoan(newLoan));
        verify(loanRepository, never()).save(any(Loan.class));
    }
    
    @Test
    void updateLoanReturnDate_ShouldUpdateSuccessfully() {
        // Arrange
        when(loanRepository.findById(1L)).thenReturn(Optional.of(testLoan1));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan1);
        when(bookRepository.save(any(Book.class))).thenReturn(testBook1);
        
        // Act
        Loan result = loanService.updateLoanReturnDate(1L, LocalDate.now());
        
        // Assert
        assertNotNull(result);
        assertEquals(LocalDate.now(), result.getReturnDate());
        assertEquals(4, testBook1.getAvailableCopies()); // Available copies should increase by 1
        verify(loanRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).save(any(Loan.class));
        verify(bookRepository, times(1)).save(any(Book.class));
    }
    
    @Test
    void updateLoanDueDate_ShouldUpdateSuccessfully() {
        // Arrange
        when(loanRepository.findById(2L)).thenReturn(Optional.of(testLoan2));
        when(loanRepository.save(any(Loan.class))).thenReturn(testLoan2);
        
        // Act
        Loan result = loanService.updateLoanDueDate(2L, LocalDate.now().plusDays(21));
        
        // Assert
        assertNotNull(result);
        verify(loanRepository, times(1)).findById(2L);
        verify(loanRepository, times(1)).save(any(Loan.class));
    }
    
    @Test
    void deleteLoan_ShouldThrowException_WhenNotFound() {
        // Arrange - Edge case
        when(loanRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class, 
            () -> loanService.deleteLoan(999L));
        verify(loanRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void getAllLoans_ShouldReturnListOfLoans() {
        // Arrange
        when(loanRepository.findAll()).thenReturn(Arrays.asList(testLoan1, testLoan2, testLoan3));
        
        // Act
        List<Loan> results = loanService.getAllLoans();
        
        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals(testLoan1.getId(), results.get(0).getId());
        assertEquals(testLoan2.getId(), results.get(1).getId());
        assertEquals(testLoan3.getId(), results.get(2).getId());
        verify(loanRepository, times(1)).findAll();
    }
    
    @Test
    void getLoansByClientId_ShouldReturnClientLoans() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient1));
        when(loanRepository.findByClient_Id(1L)).thenReturn(Arrays.asList(testLoan1, testLoan2));
        
        // Act
        List<Loan> results = loanService.getLoansByClientId(1L);
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(l -> l.getClient().getId().equals(1L)));
        verify(clientRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).findByClient_Id(1L);
    }
    
    @Test
    void getLoansByBookId_ShouldReturnBookLoans() {
        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook1));
        when(loanRepository.findByBook_Id(1L)).thenReturn(Arrays.asList(testLoan1, testLoan3));
        
        // Act
        List<Loan> results = loanService.getLoansByBookId(1L);
        
        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.stream().allMatch(l -> l.getBook().getId().equals(1L)));
        verify(bookRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).findByBook_Id(1L);
    }
    
    @Test
    void getLoansByClientIdOverdue_ShouldReturnOverdueLoans() {
        // Arrange
        when(clientRepository.findById(1L)).thenReturn(Optional.of(testClient1));
        when(loanRepository.findByClient_Id(1L)).thenReturn(Arrays.asList(testLoan1, testLoan2));
        
        // Act
        List<Loan> results = loanService.getLoansByClientIdOverdue(1L);
        
        // Assert
        assertNotNull(results);
        assertEquals(1, results.size()); // Only testLoan2 is overdue (due date is in the past and not returned)
        assertEquals(testLoan2.getId(), results.get(0).getId());
        verify(clientRepository, times(1)).findById(1L);
        verify(loanRepository, times(1)).findByClient_Id(1L);
    }
}
