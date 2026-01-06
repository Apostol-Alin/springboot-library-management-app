package aapostol.libraryManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;

@Service
public interface CategoryService {
    public List<Category> getAllCategories();
    public Category getCategoryById(Long id);
    public Category addCategory(Category category);
    public Category updateCategoryDescription(Long id, String description);
    public void deleteCategoryById(Long id);
    public List<Book> getCategoryBooks(Long categoryId);
}
