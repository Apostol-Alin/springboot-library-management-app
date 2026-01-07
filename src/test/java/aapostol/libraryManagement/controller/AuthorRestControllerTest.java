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

import aapostol.libraryManagement.json.Author;
import aapostol.libraryManagement.mapper.AuthorMapper;
import aapostol.libraryManagement.service.AuthorService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthorRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AuthorService authorService;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllAuthors_returns200() throws Exception {
        when(authorService.getAllAuthors()).thenReturn(List.of());

        mockMvc.perform(get("/author-management/authors"))
                .andExpect(status().isOk());
    }

    @Test
    void getAuthorById_returns200() throws Exception {
        when(authorService.getAuthorById(1L)).thenReturn(new Author(1L, "A", "B"));

        mockMvc.perform(get("/author-management/authors/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteAuthorById_returns204() throws Exception {
        mockMvc.perform(delete("/author-management/authors/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void searchAuthorsByName_returns200() throws Exception {
        when(authorService.searchAuthorsByName("row")).thenReturn(List.of());

        mockMvc.perform(get("/author-management/authors/name").param("name", "row"))
                .andExpect(status().isOk());
    }

    @Test
    void addAuthor_returns201() throws Exception {
        Author mapped = new Author();
        Author saved = Mockito.mock(Author.class);
        when(saved.getId()).thenReturn(1L);

        when(authorMapper.toEntity(any())).thenReturn(mapped);
        when(authorService.addAuthor(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "name", "J.K. Rowling",
                "biography", "Bio"
        ));

        mockMvc.perform(post("/author-management/authors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void updateAuthorBiography_returns200() throws Exception {
        when(authorService.updateAuthorBiography(anyLong(), eq("New bio"))).thenReturn(new Author(1L, "A", "New bio"));

        String json = objectMapper.writeValueAsString(Map.of("biography", "New bio"));

        mockMvc.perform(patch("/author-management/authors/biography")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
