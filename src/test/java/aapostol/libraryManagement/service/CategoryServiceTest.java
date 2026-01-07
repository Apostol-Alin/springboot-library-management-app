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
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.repository.JPACategoryRepository;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    
    @Mock
    private JPACategoryRepository categoryRepository;
    
    @InjectMocks
    private CategoryServiceImpl categoryService;
    
    private Category testCategory1;
    private Category testCategory2;
    private Category testCategory3;
    
    @BeforeEach
    void setUp() {
        testCategory1 = new Category(1L, "Science Fiction", "Sci-fi books");
        testCategory2 = new Category(2L, "Mystery", "Mystery and thriller books");
        testCategory3 = new Category(3L, "Romance", "Romance and love stories");
    }
    
    @Test
    void getAllCategories_ShouldReturnListOfCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(Arrays.asList(testCategory1, testCategory2, testCategory3));
        
        // Act
        List<Category> results = categoryService.getAllCategories();
        
        // Assert
        assertNotNull(results);
        assertEquals(3, results.size());
        assertEquals("Science Fiction", results.get(0).getName());
        assertEquals("Mystery", results.get(1).getName());
        assertEquals("Romance", results.get(2).getName());
        verify(categoryRepository, times(1)).findAll();
    }
    
    @Test
    void getCategoryById_ShouldReturnCategory_WhenExists() {
        // Arrange
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(testCategory2));
        
        // Act
        Category result = categoryService.getCategoryById(2L);
        
        // Assert
        assertNotNull(result);
        assertEquals("Mystery", result.getName());
        assertEquals("Mystery and thriller books", result.getDescription());
        verify(categoryRepository, times(1)).findById(2L);
    }
    
    @Test
    void addCategory_ShouldThrowException_WhenDuplicateName() {
        // Arrange - Edge case: duplicate category
        Category duplicateCategory = new Category(null, "Science Fiction", "Different description");
        when(categoryRepository.findByNameIgnoreCase("Science Fiction"))
            .thenReturn(Arrays.asList(testCategory1));
        
        // Act & Assert
        DuplicateResourceException exception = assertThrows(DuplicateResourceException.class, 
            () -> categoryService.addCategory(duplicateCategory));
        assertTrue(exception.getMessage().contains("Science Fiction"));
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    void updateCategoryDescription_ShouldUpdateSuccessfully() {
        // Arrange
        when(categoryRepository.findById(3L)).thenReturn(Optional.of(testCategory3));
        when(categoryRepository.save(any(Category.class))).thenReturn(testCategory3);
        
        // Act
        Category result = categoryService.updateCategoryDescription(3L, "Updated: Romantic novels and stories");
        
        // Assert
        assertNotNull(result);
        assertEquals("Romance", result.getName());
        verify(categoryRepository, times(1)).findById(3L);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }
    
    @Test
    void deleteCategoryById_ShouldThrowException_WhenNotFound() {
        // Arrange - Edge case: deleting non-existent category
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class, 
            () -> categoryService.deleteCategoryById(999L));
        verify(categoryRepository, never()).deleteById(anyLong());
    }
    
    @Test
    void getCategoryBooks_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange - Edge case
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(NoSuchElementException.class,
            () -> categoryService.getCategoryBooks(999L));
        verify(categoryRepository, times(1)).findById(999L);
    }
    
    @Test
    void addCategory_ShouldSaveSuccessfully_WhenCategoryIsNew() {
        // Arrange
        Category newCategory = new Category(null, "Biography", "Life stories and biographies");
        when(categoryRepository.findByNameIgnoreCase("Biography")).thenReturn(Collections.emptyList());
        when(categoryRepository.save(newCategory)).thenReturn(new Category(4L, "Biography", "Life stories and biographies"));
        
        // Act
        Category result = categoryService.addCategory(newCategory);
        
        // Assert
        assertNotNull(result);
        assertEquals("Biography", result.getName());
        verify(categoryRepository, times(1)).findByNameIgnoreCase("Biography");
        verify(categoryRepository, times(1)).save(newCategory);
    }
    
    @Test
    void getCategoryById_ShouldReturnNull_WhenNotFound() {
        // Arrange - Edge case
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act
        Category result = categoryService.getCategoryById(999L);
        
        // Assert
        assertNull(result);
        verify(categoryRepository, times(1)).findById(999L);
    }
    
    @Test
    void updateCategoryDescription_ShouldThrowException_WhenCategoryNotFound() {
        // Arrange - Edge case
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());
        
        // Act & Assert
        NoSuchElementException exception = assertThrows(NoSuchElementException.class,
            () -> categoryService.updateCategoryDescription(999L, "New description"));
        assertTrue(exception.getMessage().contains("999"));
        assertTrue(exception.getMessage().contains("not found"));
        verify(categoryRepository, never()).save(any(Category.class));
    }
    
    @Test
    void deleteCategoryById_ShouldDeleteSuccessfully_WhenCategoryExists() {
        // Arrange
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(testCategory2));
        doNothing().when(categoryRepository).deleteById(2L);
        
        // Act
        categoryService.deleteCategoryById(2L);
        
        // Assert
        verify(categoryRepository, times(1)).findById(2L);
        verify(categoryRepository, times(1)).deleteById(2L);
    }
}
