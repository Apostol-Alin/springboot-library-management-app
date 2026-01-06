package aapostol.libraryManagement.service;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;

import java.util.List;
import java.util.Optional;

@Service
public interface ClientService {

    public List<Client> getAllClients();
    public List<Client> getClientsByName(String name);
    public Client getClientById(Long id);
    public Client getClientByPhone(String phone);
    public Client addClient(Client client);
    public Client updateClientPhone(Long id, String newPhone);
    public void deleteClient(Long id);
    public Review addReviewToClient(Review review);
    public List<Review> getReviewsByClientId(Long clientId);
    public Optional<Review> getReviewById(Long id);
    public void deleteReview(Long id);
}
