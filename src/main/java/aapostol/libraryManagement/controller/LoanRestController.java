package aapostol.libraryManagement.controller;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

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
import jakarta.validation.Valid;

@RestController
@RequestMapping("/loan-management/loans")
public class LoanRestController {
    @Autowired
    private LoanService loanService;
    @Autowired
    private LoanMapper loanMapper;

    @GetMapping
    public ResponseEntity<List<Loan>> getAllLoans(){
        List<Loan> loans = this.loanService.getAllLoans();
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/client-id")
    public ResponseEntity<List<Loan>> getLoansByClientId(@RequestParam(value = "client-id") Long clientId){
        List<Loan> loans = this.loanService.getLoansByClientId(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/book-id")
    public ResponseEntity<List<Loan>> getLoansByBookId(@RequestParam(value = "book-id") Long bookId){
        List<Loan> loans = this.loanService.getLoansByBookId(bookId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @GetMapping(value = "/id")
    public ResponseEntity<Loan> getLoanById(@RequestParam(value = "id") Long id) {
        Loan loan = this.loanService.getLoanById(id);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(loan);
    }

    @GetMapping(value = "/client-id/overdue")
    public ResponseEntity<List<Loan>> getLoansByClientIdOverdue(@RequestParam(value = "client-id") Long clientId){
        List<Loan> loans = this.loanService.getLoansByClientIdOverdue(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(loans);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Loan> addLoan(@RequestBody @Valid LoanRequest requestLoan) {
        Loan loan = loanMapper.toEntity(requestLoan);
        Loan savedLoan = this.loanService.addLoan(loan);
        return ResponseEntity.created(URI.create("/id?id=" + savedLoan.getId())).body(savedLoan);
    }

    @PatchMapping(value = "/return")
    public ResponseEntity<Loan> returnBook(@RequestParam(value = "id") Long id) {
        Loan updatedLoan = this.loanService.updateLoanReturnDate(id, new java.util.Date());
        return ResponseEntity.status(HttpStatus.OK).body(updatedLoan);
    }

    @PatchMapping(value = "/due-date")
    public ResponseEntity<Loan> updateDueDate(@RequestParam(value = "id") Long id,
                                              @RequestParam(value = "due-date") String dueDateStr) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date dueDate = formatter.parse(dueDateStr);
            Loan updatedLoan = this.loanService.updateLoanDueDate(id, dueDate);
            return ResponseEntity.status(HttpStatus.OK).body(updatedLoan);
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteLoan(@RequestParam(value = "id") Long id) {
        this.loanService.deleteLoan(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}