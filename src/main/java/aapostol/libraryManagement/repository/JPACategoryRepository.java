package aapostol.libraryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Category;
import java.util.List;

public interface JPACategoryRepository extends JpaRepository<Category, Long>{
    public List<Category> findByNameIgnoreCase(String name);
}
