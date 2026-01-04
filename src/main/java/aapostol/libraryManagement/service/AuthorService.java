package aapostol.libraryManagement.service;

import java.util.List;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Author;

@Service
public interface AuthorService {
    public List<Author> getAllAuthors();
    public List<Author> searchAuthorsByName(String Name);
    public Author addAuthor(Author author);
    public Author updateAuthorBiography(Long id, String biography);
    public Author getAuthorById(Long id);
}
