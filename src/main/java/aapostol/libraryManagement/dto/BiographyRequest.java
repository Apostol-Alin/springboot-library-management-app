package aapostol.libraryManagement.dto;

import jakarta.validation.constraints.Size;

public class BiographyRequest {
    @Size(max = 511, message="Biography must not exceed 511 characters")
    private String biography;

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}