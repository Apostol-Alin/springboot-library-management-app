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

import aapostol.libraryManagement.dto.CategoryDescriptionRequest;
import aapostol.libraryManagement.dto.CategoryRequest;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.mapper.CategoryMapper;
import aapostol.libraryManagement.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/category-management/categories")
@Tag(name = "Category Management", description = "APIs for managing book categories")
public class CategoryRestController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CategoryMapper categoryMapper;
    
    @GetMapping
    @Operation(summary = "Get all categories", description = "Retrieve a list of all categories in the system. If no categories are found, an empty list is returned.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.getAllCategories());
    }

    @GetMapping(value = "/id")
    @Operation(summary = "Get Category by ID", description = "Retrieve a category by its unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the category")
    @ApiResponse(responseCode = "404", description = "Category not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Category> getCategoryById(@RequestParam(value = "id") Long id) {
        Category requestedCategory = categoryService.getCategoryById(id);

        if (requestedCategory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(requestedCategory);
    }

    @GetMapping(value = "/id/books")
    @Operation(summary = "Get Books by Category ID", description = "Retrieve a list of books associated with the category identified by the given ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of books for the category")
    @ApiResponse(responseCode = "404", description = "Category not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<List<Book>> getCategoryBooks(@RequestParam(value = "id") Long id) {
        List<Book> books = this.categoryService.getCategoryBooks(id);
        return ResponseEntity.status(HttpStatus.OK).body(books);
    }

    @DeleteMapping(value = "/id")
    @Operation(summary = "Delete Category by ID", description = "Delete a category by its unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the category")
    @ApiResponse(responseCode = "404", description = "Category not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Void> deleteCategoryById(@RequestParam(value = "id") Long id) {
        this.categoryService.deleteCategoryById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new Category", description = "Create a new category in the system")
    @ApiResponse(responseCode = "201", description = "Successfully added the category")
    @ApiResponse(responseCode = "409", description = "Category with the same name already exists", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Category> addCategory(@RequestBody @Valid CategoryRequest category) {
        Category categoryEntity = categoryMapper.toEntity(category);
        Category addedCategory = this.categoryService.addCategory(categoryEntity);
        return ResponseEntity.created(URI.create("/id?id=" + addedCategory.getId())).body(addedCategory);
    }

    @PatchMapping(value = "/description")
    @Operation(summary = "Update Category Description", description = "Update the description of an existing category identified by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the category description")
    @ApiResponse(responseCode = "404", description = "Category not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    public ResponseEntity<Category> updateCategoryDescription(@RequestParam(value = "id") Long id, @RequestBody @Valid CategoryDescriptionRequest descriptionRequest) {
        Category updatedCategory = this.categoryService.updateCategoryDescription(id, descriptionRequest.getDescription());
        return ResponseEntity.status(HttpStatus.OK).body(updatedCategory);
    }

}