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

import aapostol.libraryManagement.dto.AuthorRequest;
import aapostol.libraryManagement.dto.BiographyRequest;
import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.mapper.AuthorMapper;
import aapostol.libraryManagement.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("author-management/authors")
@Tag(name = "Author Management", description = "API endpoints for managing authors")
public class AuthorRestController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private AuthorMapper authorMapper;

    @GetMapping
    @Operation(summary = "Get All Authors", description = "Retrieve a list of all authors in the system. If no authors are found, an empty list is returned.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of authors")
    public ResponseEntity<List<Author>> getAllAuthors(){
        List<Author> authorList = this.authorService.getAllAuthors();
        return ResponseEntity.status(HttpStatus.OK).body(authorList);
    }

    @Operation(summary = "Get Author by ID", description = "Retrieve an author by their unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the author")
    @ApiResponse(responseCode = "404", description = "Author not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    @GetMapping(value = "/id")
    public ResponseEntity<Author> getAuthorById(@RequestParam(value = "id") Long id) {
        Author author = this.authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }   
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @Operation(summary = "Delete Author by ID", description = "Delete an author by their unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the author")
    @ApiResponse(responseCode = "404", description = "Author not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteAuthorById(@RequestParam(value = "id") Long id) {
        this.authorService.deleteAuthorById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @Operation(summary = "Search Authors by Name", description = "Search authors whose names contain the specified string case-insensitively. Returns a list of authors matching the search criteria.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of authors matching the search criteria")
    @GetMapping(value = "/name")
    public ResponseEntity<List<Author>> searchAuthorsByName(@RequestParam(value = "name") @Size(max = 255) String name) {
        List<Author> authorList = this.authorService.searchAuthorsByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(authorList);
    }

    @Operation(summary = "Add New Author", description = "Add a new author to the system. Returns the created author with its assigned ID.")
    @ApiResponse(responseCode = "201", description = "Successfully added the new author")
    @ApiResponse(responseCode = "409", description = "An author with the same name already exists", content = @Content(schema = @Schema(implementation = Void.class)))
    @PostMapping(value = "/add")
    public ResponseEntity<Author> addAuthor(@RequestBody @Valid AuthorRequest authorRequest) {
        Author author = this.authorMapper.toEntity(authorRequest);
        Author addedAuthor = this.authorService.addAuthor(author);
        return ResponseEntity.created(URI.create("/id?id=" + addedAuthor.getId())).body(addedAuthor);
    }

    @Operation(summary = "Update Author Biography", description = "Update the biography of an existing author identified by their unique ID. Returns the updated author.")
    @ApiResponse(responseCode = "200", description = "Successfully updated the author's biography")
    @ApiResponse(responseCode = "404", description = "Author not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)))
    @PatchMapping(value = "/biography")
    public ResponseEntity<Author> updateAuthorBiography(@RequestParam(value = "id") Long id, @RequestBody @Valid BiographyRequest biographyRequest) {
        Author updatedAuthor = this.authorService.updateAuthorBiography(id, biographyRequest.getBiography());
        return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
    }

}
