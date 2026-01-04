package aapostol.libraryManagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Author;

public interface JPAAuthorRepository extends JpaRepository<Author, Long>{
    public List<Author> findByNameContainingIgnoreCase(String Name);
    public List<Author> findByNameIgnoreCase(String Name);
}
