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

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.service.CategoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/category-management/categories")
public class CategoryRestController {
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @GetMapping(value = "/id")
    public ResponseEntity<Category> getCategoryById(@RequestParam(value = "id") Long id) {
        Category requestedCategory = categoryService.getCategoryById(id);

        if (requestedCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(requestedCategory);
    }

    @GetMapping(value = "/id/books")
    public ResponseEntity<List<Book>> getCategoryBooks(@RequestParam(value = "id") Long id) {
        List<Book> books = this.categoryService.getCategoryBooks(id);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteCategoryById(@RequestParam(value = "id") Long id) {
        this.categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Category> addCategory(@RequestBody @Valid Category category) {
        Category addedCategory = this.categoryService.addCategory(category);
        return ResponseEntity.created(URI.create("/id?id=" + addedCategory.getId())).body(addedCategory);
    }

    @PatchMapping(value = "/description")
    public ResponseEntity<Category> updateCategoryDescription(@RequestParam(value = "id") Long id, @RequestParam(value = "description") @Size(max = 511) String description) {
        Category updatedCategory = this.categoryService.updateCategoryDescription(id, description);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

}