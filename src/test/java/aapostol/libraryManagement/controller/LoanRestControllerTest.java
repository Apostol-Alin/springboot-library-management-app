package aapostol.libraryManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import aapostol.libraryManagement.json.Loan;
import aapostol.libraryManagement.mapper.LoanMapper;
import aapostol.libraryManagement.service.LoanService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private LoanService loanService;

    @Mock
    private LoanMapper loanMapper;

    @InjectMocks
    private LoanRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllLoans_returns200() throws Exception {
        when(loanService.getAllLoans()).thenReturn(List.of());

        mockMvc.perform(get("/loan-management/loans"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoansByClientId_returns200() throws Exception {
        when(loanService.getLoansByClientId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/loan-management/loans/client-id").param("client-id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoansByBookId_returns200() throws Exception {
        when(loanService.getLoansByBookId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/loan-management/loans/book-id").param("book-id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoanById_returns200() throws Exception {
        when(loanService.getLoanById(1L)).thenReturn(new Loan());

        mockMvc.perform(get("/loan-management/loans/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getLoansByClientIdOverdue_returns200() throws Exception {
        when(loanService.getLoansByClientIdOverdue(1L)).thenReturn(List.of());

        mockMvc.perform(get("/loan-management/loans/client-id/overdue").param("client-id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addLoan_returns201() throws Exception {
        Loan mapped = new Loan();
        Loan saved = Mockito.mock(Loan.class);
        when(saved.getId()).thenReturn(1L);

        when(loanMapper.toEntity(any())).thenReturn(mapped);
        when(loanService.addLoan(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "bookId", "1",
                "clientId", "1",
                "dueDate", "2099-12-31"
        ));

        mockMvc.perform(post("/loan-management/loans/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void returnBook_returns200() throws Exception {
        when(loanService.updateLoanReturnDate(anyLong(), any())).thenReturn(new Loan());

        mockMvc.perform(patch("/loan-management/loans/return").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void updateDueDate_returns200() throws Exception {
        when(loanService.updateLoanDueDate(anyLong(), any())).thenReturn(new Loan());

        mockMvc.perform(patch("/loan-management/loans/due-date")
                        .param("id", "1")
                        .param("due-date", "2099-12-31"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteLoan_returns204() throws Exception {
        mockMvc.perform(delete("/loan-management/loans/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }
}
