package aapostol.libraryManagement.dto;

import jakarta.validation.constraints.Size;

public class CategoryDescriptionRequest {
    @Size(max = 511, message="Category description must not exceed 511 characters")
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
