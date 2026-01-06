package aapostol.libraryManagement.json;

import java.util.Set;

import org.json.JSONObject;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Name", nullable = false, length = 100, unique = true)
    @NotBlank(message = "Category name cannot be blank")
    @NotNull(message = "Category name cannot be null")
    @Size(max = 100, message = "Category name must be less than 101 characters")
    private String name;

    @Column(name = "Description", nullable = true, length = 511)
    @Size(max = 511, message = "Category description must be less than 512 characters")
    private String description;

    @ManyToMany(mappedBy = "categories")
    private Set<Book> books;

    public Category() {
    }
    public Category(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
        this.description = null;
    }
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        JSONObject categoryJson = new JSONObject();
        categoryJson.put("id", this.id);
        categoryJson.put("name", this.name);
        categoryJson.put("description", this.description);
        return categoryJson.toString();
    }
}
