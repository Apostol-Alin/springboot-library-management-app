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

import aapostol.libraryManagement.json.Category;
import aapostol.libraryManagement.mapper.CategoryMapper;
import aapostol.libraryManagement.service.CategoryService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoryRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CategoryService categoryService;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllCategories_returns200() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());

        mockMvc.perform(get("/category-management/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById_returns200() throws Exception {
        when(categoryService.getCategoryById(1L)).thenReturn(new Category(1L, "Sci", "Desc"));

        mockMvc.perform(get("/category-management/categories/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryBooks_returns200() throws Exception {
        when(categoryService.getCategoryBooks(1L)).thenReturn(List.of());

        mockMvc.perform(get("/category-management/categories/id/books").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategoryById_returns204() throws Exception {
        mockMvc.perform(delete("/category-management/categories/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void addCategory_returns201() throws Exception {
        Category mapped = new Category();
        Category saved = Mockito.mock(Category.class);
        when(saved.getId()).thenReturn(1L);

        when(categoryMapper.toEntity(any())).thenReturn(mapped);
        when(categoryService.addCategory(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "name", "Science Fiction",
                "description", "Desc"
        ));

        mockMvc.perform(post("/category-management/categories/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void updateCategoryDescription_returns200() throws Exception {
        when(categoryService.updateCategoryDescription(anyLong(), eq("New"))).thenReturn(new Category(1L, "Sci", "New"));

        String json = objectMapper.writeValueAsString(Map.of("description", "New"));

        mockMvc.perform(patch("/category-management/categories/description")
                        .param("id", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());
    }
}
