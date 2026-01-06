package aapostol.libraryManagement.mapper;

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.repository.JPABookRepository;
import aapostol.libraryManagement.repository.JPAReviewRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import aapostol.libraryManagement.dto.ReviewRequest;

@Component
public class ReviewMapper {
    @Autowired
    private JPABookRepository bookRepository;

    @Autowired
    private JPAClientRepository clientRepository;

    @Autowired
    private JPAReviewRepository reviewRepository;

    public Review toEntity(ReviewRequest reviewRequest) {
        Review review = new Review();
        Book book = bookRepository.findById(reviewRequest.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book with ID " + reviewRequest.getBookId() + " not found."));
        Client client = clientRepository.findById(reviewRequest.getClientId())
                .orElseThrow(() -> new IllegalArgumentException("Client with ID " + reviewRequest.getClientId() + " not found."));
        Optional<Review> existingReview = reviewRepository.findByClient_IdAndBook_Id(reviewRequest.getClientId(), reviewRequest.getBookId());
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("Review by Client ID " + reviewRequest.getClientId() + " for Book ID " + reviewRequest.getBookId() + " already exists.");
        }  
        review.setBook(book);
        review.setClient(client);
        review.setReviewText(reviewRequest.getReviewText());
        review.setReviewDate(new java.util.Date());
        return review;
    }
}