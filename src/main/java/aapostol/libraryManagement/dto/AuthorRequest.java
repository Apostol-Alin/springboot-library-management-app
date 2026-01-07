package aapostol.libraryManagement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Request object for creating an author")
public class AuthorRequest {

    @Schema(description = "Name of the author", example = "J.K. Rowling")
    @NotBlank(message = "Author name cannot be blank")
    @NotNull(message = "Author name cannot be null")
    @Size(max = 255, message = "Author name must be less than 256 characters")
    private String name;

    @Schema(description = "Biography of the author", example = "British author best known for the Harry Potter series")
    @Size(max = 511, message = "Author biography must be less than 512 characters")
    private String biography;

    public AuthorRequest() {
    }

    public AuthorRequest(String name, String biography) {
        this.name = name;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}