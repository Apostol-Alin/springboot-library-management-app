package aapostol.libraryManagement.json;

import org.json.JSONObject;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Authors")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the author", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(name = "Name", nullable = false, length = 255)
    @NotBlank(message = "Author name cannot be blank")
    @NotNull(message = "Author name cannot be null")
    @Size(max = 255, message = "Author name must be less than 256 characters")
    @Schema(description = "Name of the author", example = "J.K. Rowling")
    private String name;

    @Column(name = "Biography", nullable = true, length = 511)
    @Size(max = 511, message = "Author biography must be less than 512 characters")
    @Schema(description = "Biography of the author", example = "British author best known for the Harry Potter series")
    private String biography;

    public Author(Long id, String name, String biography) {
        this.id = id;
        this.name = name;
        this.biography = biography;
    }

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
        this.biography = null;
    }

    public Author() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        json.put("id", id);
        json.put("name", name);
        json.put("biography", biography);
        return json.toString();
    }
}
