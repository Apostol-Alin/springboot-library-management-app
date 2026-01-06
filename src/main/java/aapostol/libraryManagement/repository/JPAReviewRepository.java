package aapostol.libraryManagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Review;

public interface JPAReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByClient_Id(Long clientId);
    List<Review> findByBook_Id(Long bookId);
    Optional<Review> findByClient_IdAndBook_Id(Long clientId, Long bookId);
}
