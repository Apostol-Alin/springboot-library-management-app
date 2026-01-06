package aapostol.libraryManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Book;

public interface JPABookRepository extends JpaRepository<Book, Long>{
    public List<Book> findByTitleContainingIgnoreCase(String title);
    public List<Book> findByAuthor_idIn(List<Long> authorIds);
    public List<Book> findByAuthor_id(Long authorId);
    public List<Book> findByCategoriesId(Long categoryId);
}
