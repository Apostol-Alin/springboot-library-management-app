package aapostol.libraryManagement.controller;

import java.net.URI;
import java.util.List;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import aapostol.libraryManagement.dto.LoanRequest;
import aapostol.libraryManagement.json.Loan;
import aapostol.libraryManagement.mapper.LoanMapper;
import aapostol.libraryManagement.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/loan-management/loans")
@Tag(name = "Loan Management", description = "APIs for managing book loans")
public class LoanRestController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanMapper loanMapper;

    @GetMapping
    @Operation(summary = "Get all loans", description = "Retrieve a list of all book loans")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of loans")
    public ResponseEntity<List<Loan>> getAllLoans(){
        List<Loan> loans = this.loanService.getAllLoans();
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/client-id")
    @Operation(summary = "Get loans by client ID", description = "Retrieve a list of loans for a specific client by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of loans for the client")
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<List<Loan>> getLoansByClientId(@RequestParam(value = "client-id") Long clientId){
        List<Loan> loans = this.loanService.getLoansByClientId(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/book-id")
    @Operation(summary = "Get loans by book ID", description = "Retrieve a list of loans for a specific book by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of loans for the book")
    @ApiResponse(responseCode = "404", description = "Book not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<List<Loan>> getLoansByBookId(@RequestParam(value = "book-id") Long bookId){
        List<Loan> loans = this.loanService.getLoansByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/id")
    @Operation(summary = "Get loan by ID", description = "Retrieve a specific loan by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the loan")
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Loan> getLoanById(@RequestParam(value = "id") Long id) {
        Loan loan = this.loanService.getLoanById(id);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }

    @GetMapping(value = "/client-id/overdue")
    @Operation(summary = "Get overdue loans by client ID", description = "Retrieve a list of overdue loans for a specific client by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of overdue loans for the client")
    @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<List<Loan>> getLoansByClientIdOverdue(@RequestParam(value = "client-id") Long clientId){
        List<Loan> loans = this.loanService.getLoansByClientIdOverdue(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new loan", description = "Create a new book loan")
    @ApiResponse(responseCode = "201", description = "Successfully created the loan")
    @ApiResponse(responseCode = "400", description = "No available copies for the requested book")
    @ApiResponse(responseCode = "404", description = "Book or Client not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    @ApiResponse(responseCode = "409", description = "Client has already borrowed this book and has not returned it yet")
    public ResponseEntity<Loan> addLoan(@RequestBody @Valid LoanRequest requestLoan) {
        Loan loan = loanMapper.toEntity(requestLoan);
        Loan savedLoan = this.loanService.addLoan(loan);
        return ResponseEntity.created(URI.create("/id?id=" + savedLoan.getId())).body(savedLoan);
    }

    @PatchMapping(value = "/return")
    @Operation(summary = "Return a book", description = "Update the return date of a loan to mark the book as returned")
    @ApiResponse(responseCode = "200", description = "Successfully updated the loan with return date")
    @ApiResponse(responseCode = "400", description = "Loan has already been returned")
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Loan> returnBook(@RequestParam(value = "id") Long id) {
        Loan updatedLoan = this.loanService.updateLoanReturnDate(id, LocalDate.now());
        return ResponseEntity.status(HttpStatus.OK).body(updatedLoan);
    }

    @PatchMapping(value = "/due-date")
    @Operation(summary = "Update loan due date", description = "Update the due date of a loan")
    @ApiResponse(responseCode = "200", description = "Successfully updated the loan due date")
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Loan> updateDueDate(@RequestParam(value = "id") Long id,
                                              @RequestParam(value = "due-date") LocalDate dueDate) {
        Loan updatedLoan = this.loanService.updateLoanDueDate(id, dueDate);
        return ResponseEntity.status(HttpStatus.OK).body(updatedLoan);
    }

    @DeleteMapping(value = "/id")
    @Operation(summary = "Delete a loan", description = "Delete a specific loan by its ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the loan")
    @ApiResponse(responseCode = "404", description = "Loan not found", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Void> deleteLoan(@RequestParam(value = "id") Long id) {
        this.loanService.deleteLoan(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}