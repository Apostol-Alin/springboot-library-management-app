package aapostol.libraryManagement.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aapostol.libraryManagement.dto.BookRequest;
import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.repository.JPAAuthorRepository;

@Component
public class BookMapper {
    @Autowired
    private JPAAuthorRepository authorRepository;

    public Book toEntity(BookRequest bookRequest) {
        Book book = new Book();
        Author author = authorRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new IllegalArgumentException("Author with ID " + bookRequest.getAuthorId() + " not found."));
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getDescription());
        book.setPublicationDate(bookRequest.getPublicationDate());
        book.setTotalCopies(bookRequest.getTotalCopies());
        book.setAvailableCopies(bookRequest.getAvailableCopies());
        book.setAuthor(author);
        return book;
    }
}
