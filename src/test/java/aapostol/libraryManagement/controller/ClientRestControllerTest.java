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

import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.mapper.ClientMapper;
import aapostol.libraryManagement.service.ClientService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClientRestControllerTest {

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private ClientService clientService;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientRestController controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void getAllClients_returns200() throws Exception {
        when(clientService.getAllClients()).thenReturn(List.of());

        mockMvc.perform(get("/client-management/clients"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientById_returns200() throws Exception {
        when(clientService.getClientById(1L)).thenReturn(new Client());

        mockMvc.perform(get("/client-management/clients/id").param("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientsByName_returns200() throws Exception {
        when(clientService.getClientsByName("john")).thenReturn(List.of());

        mockMvc.perform(get("/client-management/clients/name").param("name", "john"))
                .andExpect(status().isOk());
    }

    @Test
    void getClientByPhone_returns200() throws Exception {
        when(clientService.getClientByPhone("+123")).thenReturn(new Client());

        mockMvc.perform(get("/client-management/clients/phone").param("phone", "+123"))
                .andExpect(status().isOk());
    }

    @Test
    void addClient_returns201() throws Exception {
        Client mapped = new Client();
        Client saved = Mockito.mock(Client.class);
        when(saved.getId()).thenReturn(1L);

        when(clientMapper.toEntity(any())).thenReturn(mapped);
        when(clientService.addClient(eq(mapped))).thenReturn(saved);

        String json = objectMapper.writeValueAsString(Map.of(
                "name", "John Doe",
                "phone", "+1234567890"
        ));

        mockMvc.perform(post("/client-management/clients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void updateClientPhone_returns200() throws Exception {
        when(clientService.updateClientPhone(anyLong(), eq("+999"))).thenReturn(new Client());

        mockMvc.perform(patch("/client-management/clients/phone")
                        .param("id", "1")
                        .param("phone", "+999"))
                .andExpect(status().isOk());
    }

    @Test
    void deleteClient_returns204() throws Exception {
        mockMvc.perform(delete("/client-management/clients/id").param("id", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void getReviewsByClientId_returns200() throws Exception {
        when(clientService.getReviewsByClientId(1L)).thenReturn(List.of());

        mockMvc.perform(get("/client-management/clients/reviews").param("client-id", "1"))
                .andExpect(status().isOk());
    }
}
