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

import aapostol.libraryManagement.json.Book;
import aapostol.libraryManagement.mapper.BookMapper;
import aapostol.libraryManagement.service.BookService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BookRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private BookService bookService;

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllBooks_returns200() throws Exception {
        when(bookService.getAllBooks()).thenReturn(List.of());

        mockMvc.perform(get("/book-management/books"))
                .andExpect(status().isOk());
    }

    @Test
    void getBookById_returns200() throws Exception {
        when(bookService.getBookById(1L)).thenReturn(new Book());

        mockMvc.perform(get("/book-management/books/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteBookById_returns204() throws Exception {
        mockMvc.perform(delete("/book-management/books/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getBooksByTitle_returns200() throws Exception {
        when(bookService.getBooksByTitle("hp")).thenReturn(List.of());

        mockMvc.perform(get("/book-management/books/title").param("title", "hp"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksByAuthorName_returns200() throws Exception {
        when(bookService.getBooksByAuthorName("row")).thenReturn(List.of());

        mockMvc.perform(get("/book-management/books/author-name").param("author-name", "row"))
                .andExpect(status().isOk());
    }

    @Test
    void getBooksByAuthorId_returns200() throws Exception {
        when(bookService.getBooksByAuthorId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/book-management/books/author-id").param("author-id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addBook_returns201() throws Exception {
        Book mapped = new Book();
        Book saved = Mockito.mock(Book.class);
        when(saved.getId()).thenReturn(1L);

        when(bookMapper.toEntity(any())).thenReturn(mapped);
        when(bookService.addBook(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "title", "Harry Potter",
                "description", "Desc",
                "publicationDate", "1997-06-26",
                "totalCopies", "10",
                "availableCopies", "7",
                "authorId", "1"
        ));

        mockMvc.perform(post("/book-management/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void addCategoryToBook_returns200() throws Exception {
        when(bookService.addCategoryToBook(anyLong(), anyLong())).thenReturn(new Book());

        mockMvc.perform(patch("/book-management/books/add-category")
                        .param("book-id", "1")
                        .param("category-id", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void removeCategoryFromBook_returns200() throws Exception {
        when(bookService.removeCategoryFromBook(anyLong(), anyLong())).thenReturn(new Book());

        mockMvc.perform(patch("/book-management/books/remove-category")
                        .param("book-id", "1")
                        .param("category-id", "2"))
                .andExpect(status().isOk());
    }

    @Test
    void getReviewsByBookId_returns200() throws Exception {
        when(bookService.getReviewsByBookId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/book-management/books/reviews").param("book-id", "1"))
                .andExpect(status().isOk());
    }
}
