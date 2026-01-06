package aapostol.libraryManagement.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPACategoryRepository;
import aapostol.libraryManagement.exception.*;;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private JPACategoryRepository categoryRepository;

    @Autowired
    private JPABookRepository bookRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    @Override
    public Category addCategory(Category category) {
        // find if a category with the same name (case-insensitive) already exists
        // The unique constraint doesn't check for case-insensitivity, so we have to do it manually
        List<Category> existingCategories = categoryRepository.findByNameIgnoreCase(category.getName());
        if (!existingCategories.isEmpty()) {
            throw new DuplicateResourceException("Category with name '" + category.getName() + "' already exists.");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.deleteById(id);
        }
        else {
            throw new NoSuchElementException("Category with ID " + id + " not found.");
        }
    }

    @Override
    public Category updateCategoryDescription(Long id, String description) {
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            Category updatedCategory = category.get();
            updatedCategory.setDescription(description);
            return categoryRepository.save(updatedCategory);
        }
        else {
            throw new NoSuchElementException("Category with ID " + id + " not found.");
        }
    }

    @Override
    public List<Book> getCategoryBooks(Long categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (!category.isPresent()) {
            throw new NoSuchElementException("Category with ID " + categoryId + " not found.");
        }
        return bookRepository.findByCategoriesId(categoryId);
    }

}  