package aapostol.libraryManagement.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.repository.JPAAuthorRepository;
import aapostol.libraryManagement.exception.*;;

@Service
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    private JPAAuthorRepository authorRepository;

    @Override
    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public List<Author> searchAuthorsByName(String name) {
        return authorRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElse(null);
    }

    @Override
    public Author addAuthor(Author author) {

        // Check for existing author with the same name (case-insensitive)
        List<Author> existingAuthors = authorRepository.findByNameIgnoreCase(author.getName());
        if (!existingAuthors.isEmpty()) {
            throw new DuplicateResourceException("Author with name '" + author.getName() + "' already exists.");
        }

        return authorRepository.save(author);
    }

    @Override
    public Author updateAuthorBiography(Long id, String biography) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            Author updatedAuthor = author.get();
            updatedAuthor.setBiography(biography);
            return authorRepository.save(updatedAuthor);
        }
        throw new NoSuchElementException("Author with ID " + id + " not found.");
    }

    @Override
    public void deleteAuthorById(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        if (author.isPresent()) {
            authorRepository.deleteById(id);
        }
        else {
            throw new NoSuchElementException("Author with ID " + id + " not found.");
        }
    }
}