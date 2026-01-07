package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public class BiographyRequest {
    @Size(max = 511, message="Biography must not exceed 511 characters")
    @Schema(description = "New biography of the author", example = "British author best known for the Harry Potter series")
    private String biography;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}