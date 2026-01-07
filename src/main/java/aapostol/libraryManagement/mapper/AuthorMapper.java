package aapostol.libraryManagement.mapper;

import org.springframework.stereotype.Component;

import aapostol.libraryManagement.dto.AuthorRequest;
import aapostol.libraryManagement.json.Author;

@Component
public class AuthorMapper {

    public Author toEntity(AuthorRequest authorRequest) {
        Author author = new Author();
        author.setName(authorRequest.getName());
        author.setBiography(authorRequest.getBiography());
        return author;
    }
}