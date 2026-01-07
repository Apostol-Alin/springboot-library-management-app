package aapostol.libraryManagement.controller;

import java.net.URI;
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

import aapostol.libraryManagement.dto.ClientRequest;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.mapper.ClientMapper;
import aapostol.libraryManagement.service.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/client-management/clients")
@Tag(name = "Client Management", description = "APIs for managing library clients")
public class ClientRestController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientMapper clientMapper;

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieve a list of all clients in the system. If no clients are found, an empty list is returned.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clients")
    public ResponseEntity<List<Client>> getAllClients(){
        List<Client> clients = this.clientService.getAllClients();
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @GetMapping(value = "/id")
    @Operation(summary = "Get Client by ID", description = "Retrieve a client by its unique ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the client")
    @ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Client> getClientById(@RequestParam(value = "id") Long id) {
        Client client = this.clientService.getClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @GetMapping(value = "/name")
    @Operation(summary = "Get Clients by Name", description = "Retrieve a list of clients matching the given name. If no clients are found, an empty list is returned.")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of clients")
    public ResponseEntity<List<Client>> getClientsByName(@RequestParam(value = "name") String name) {
        List<Client> clients = this.clientService.getClientsByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @GetMapping(value = "/phone")
    @Operation(summary = "Get Client by Phone Number", description = "Retrieve a client by their phone number")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the client")
    @ApiResponse(responseCode = "404", description = "Client not found with the provided phone number", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Client> getClientByPhone(@RequestParam(value = "phone") String phone) {
        Client client = this.clientService.getClientByPhone(phone);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PostMapping(value = "/add")
    @Operation(summary = "Add a new Client", description = "Create a new client in the system")
    @ApiResponse(responseCode = "201", description = "Successfully added the client")
    @ApiResponse(responseCode = "409", description = "Client with the same phone number already exists", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Client> addClient(@RequestBody @Valid ClientRequest clientRequest) {
        Client client = this.clientMapper.toEntity(clientRequest);
        Client savedClient = this.clientService.addClient(client);
        return ResponseEntity.created(URI.create("/id?id=" + savedClient.getId())).body(savedClient);
    }

    @PatchMapping(value = "/phone")
    @Operation(summary = "Update Client Phone Number", description = "Update the phone number of an existing client identified by its ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated the client phone number")
    @ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Client> updateClientPhone(@RequestParam(value = "id") Long id,
                                                    @RequestParam(value = "phone") String newPhone) {
        Client updatedClient = this.clientService.updateClientPhone(id, newPhone);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping(value = "/id")
    @Operation(summary = "Delete Client by ID", description = "Delete a client by its unique ID")
    @ApiResponse(responseCode = "204", description = "Successfully deleted the client")
    @ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<Void> deleteClient(@RequestParam(value = "id") Long id) {
        this.clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/reviews")
    @Operation(summary = "Get Reviews by Client ID", description = "Retrieve a list of reviews made by the client identified by the given ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of reviews for the client")
    @ApiResponse(responseCode = "404", description = "Client not found with the provided ID", content = @Content(schema = @Schema(implementation = Void.class)) )
    public ResponseEntity<List<Review>> getReviewsByClientId(@RequestParam(value = "client-id") Long id) {
        List<Review> reviews = this.clientService.getReviewsByClientId(id);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}