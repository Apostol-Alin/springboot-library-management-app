package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for creating or updating a category")
public class CategoryRequest {

    @Schema(description = "Name of the category", example = "Science Fiction", maxLength = 100)
    @NotBlank(message = "Category name cannot be blank")
    @NotNull(message = "Category name cannot be null")
    @Size(max = 100, message = "Category name must be less than 101 characters")
    private String name;

    @Schema(description = "Description of the category", example = "A category for science fiction books", maxLength = 511, nullable = true)
    @Size(max = 511, message = "Category description must be less than 512 characters")
    private String description;

    public CategoryRequest() {
    }

    public CategoryRequest(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}