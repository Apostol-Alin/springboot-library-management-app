package aapostol.libraryManagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.mapper.ReviewMapper;
import aapostol.libraryManagement.service.ClientService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClientService clientService;

    @Mock
    private ReviewMapper reviewMapper;

    @InjectMocks
    private ReviewRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getReviewById_returns200() throws Exception {
        when(clientService.getReviewById(1L)).thenReturn(Optional.of(new Review()));

        mockMvc.perform(get("/review-management/reviews/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addReview_returns201() throws Exception {
        Review mapped = new Review();
        Review saved = Mockito.mock(Review.class);
        when(saved.getId()).thenReturn(1L);

        when(reviewMapper.toEntity(any())).thenReturn(mapped);
        when(clientService.addReviewToClient(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "bookId", "1",
                "clientId", "1",
                "reviewText", "Great"
        ));

        mockMvc.perform(post("/review-management/reviews/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void deleteReviewById_returns204() throws Exception {
        mockMvc.perform(delete("/review-management/reviews/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }
}
