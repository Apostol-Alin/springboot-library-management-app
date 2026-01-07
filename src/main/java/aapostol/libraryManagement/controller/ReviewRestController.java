package aapostol.libraryManagement.controller;

import java.net.URI;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aapostol.libraryManagement.dto.ReviewRequest;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.mapper.ReviewMapper;
import aapostol.libraryManagement.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/review-management/reviews")
@Tag(name = "Review Management", description = "APIs for managing book reviews")
public class ReviewRestController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ReviewMapper reviewMapper;

    @GetMapping(value = "/id")
    @Operation(summary = "Get review by ID", description = "Retrieve a specific review by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the review")
    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Review> getReviewById(@RequestParam("id") Long id) {
        Optional<Review> reviewOpt = clientService.getReviewById(id);
        if (reviewOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(reviewOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new review", description = "Create a new book review")
    @ApiResponse(responseCode = "201", description = "Successfully created the review")
    @ApiResponse(responseCode = "404", description = "Client or Book not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Review> addReview(@RequestBody @Valid ReviewRequest reviewRequest) {
        Review review = this.reviewMapper.toEntity(reviewRequest);
        Review savedReview = clientService.addReviewToClient(review);
        return ResponseEntity.created(URI.create("/id?id=" + savedReview.getId())).body(savedReview);
    }

    @DeleteMapping(value = "/id")
    @Operation(summary = "Delete a review", description = "Delete a specific review by its ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the review")
    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Void> deleteReviewById(@RequestParam("id") Long id) {
        this.clientService.deleteReview(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
