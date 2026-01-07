package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class CategoryDescriptionRequest {
    @Size(max = 511, message="Category description must not exceed 511 characters")
    @Schema(description = "The new description for the category", example = "A category for science fiction books", maxLength = 511)
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
