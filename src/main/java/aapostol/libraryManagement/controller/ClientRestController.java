package aapostol.libraryManagement.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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
import aapostol.libraryManagement.dto.ReviewRequest;
import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.mapper.ClientMapper;
import aapostol.libraryManagement.mapper.ReviewMapper;
import aapostol.libraryManagement.service.ClientService;
import jakarta.validation.Valid;
@RestController
@RequestMapping("/client-management/clients")
public class ClientRestController {
    @Autowired
    private ClientService clientService;
    @Autowired
    private ClientMapper clientMapper;
    @Autowired
    private ReviewMapper reviewMapper;

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(){
        List<Client> clients = this.clientService.getAllClients();
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @GetMapping(value = "/id")
    public ResponseEntity<Client> getClientById(@RequestParam(value = "id") Long id) {
        Client client = this.clientService.getClientById(id);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @GetMapping(value = "/name")
    public ResponseEntity<List<Client>> getClientsByName(@RequestParam(value = "name") String name) {
        List<Client> clients = this.clientService.getClientsByName(name);
        return ResponseEntity.status(HttpStatus.OK).body(clients);
    }

    @GetMapping(value = "/phone")
    public ResponseEntity<Client> getClientByPhone(@RequestParam(value = "phone") String phone) {
        Client client = this.clientService.getClientByPhone(phone);
        if (client == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }

    @PostMapping(value = "/add")
    public ResponseEntity<Client> addClient(@RequestBody @Valid ClientRequest clientRequest) {
        Client client = this.clientMapper.toEntity(clientRequest);
        Client savedClient = this.clientService.addClient(client);
        return ResponseEntity.created(URI.create("/id?id=" + savedClient.getId())).body(savedClient);
    }

    @PatchMapping(value = "/phone")
    public ResponseEntity<Client> updateClientPhone(@RequestParam(value = "id") Long id,
                                                    @RequestParam(value = "phone") String newPhone) {
        Client updatedClient = this.clientService.updateClientPhone(id, newPhone);
        return ResponseEntity.status(HttpStatus.OK).body(updatedClient);
    }

    @DeleteMapping(value = "/id")
    public ResponseEntity<Void> deleteClient(@RequestParam(value = "id") Long id) {
        this.clientService.deleteClient(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/reviews")
    public ResponseEntity<List<Review>> getReviewsByClientId(@RequestParam(value = "client-id") Long id) {
        List<Review> reviews = this.clientService.getReviewsByClientId(id);
        return ResponseEntity.status(HttpStatus.OK).body(reviews);
    }
}