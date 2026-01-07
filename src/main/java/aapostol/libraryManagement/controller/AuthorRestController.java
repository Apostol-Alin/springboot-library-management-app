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

import aapostol.libraryManagement.dto.BiographyRequest;
import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.service.AuthorService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("author-management/authors")
public class AuthorRestController {
    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors(){
        List<Author> authorList = this.authorService.getAllAuthors();
        return ResponseEntity.status(HttpStatus.OK).body(authorList);
    }

    @GetMapping(value = "/id")
    public ResponseEntity<Author> getAuthorById(@RequestParam(value = "id") Long id) {
        Author author = this.authorService.getAuthorById(id);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }   
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteAuthorById(@RequestParam(value = "id") Long id) {
        this.authorService.deleteAuthorById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<Author>> searchAuthorsByName(@RequestParam(value = "name") @Size(max = 255) String name) {
        List<Author> authorList = this.authorService.searchAuthorsByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(authorList);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Author> addAuthor(@RequestBody @Valid Author author) {
        Author addedAuthor = this.authorService.addAuthor(author);
        return ResponseEntity.created(URI.create("/id?id=" + addedAuthor.getId())).body(addedAuthor);
    }

    @PatchMapping(value = "/biography")
    public ResponseEntity<Author> updateAuthorBiography(@RequestParam(value = "id") Long id, @RequestBody @Valid BiographyRequest biographyRequest) {
        Author updatedAuthor = this.authorService.updateAuthorBiography(id, biographyRequest.getBiography());
        return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
    }

}
