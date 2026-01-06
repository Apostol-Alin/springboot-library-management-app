package aapostol.libraryManagement.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Client;
import aapostol.libraryManagement.json.Review;
import aapostol.libraryManagement.repository.JPAClientRepository;
import aapostol.libraryManagement.repository.JPAReviewRepository;
import aapostol.libraryManagement.exception.*;;

@Service
public class ClientServiceImpl implements ClientService {
    @Autowired
    private JPAClientRepository clientRepository;
    @Autowired
    private JPAReviewRepository reviewRepository;

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public List<Client> getClientsByName(String name) {
        return clientRepository.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client getClientByPhone(String phone) {
        return clientRepository.findByPhone(phone).orElse(null);
    }

    @Override
    public Client addClient(Client client) {
        // Check for existing client with the same phone number
        Optional<Client> existingClient = clientRepository.findByPhone(client.getPhone());
        if (existingClient.isPresent()) {
            throw new DuplicateResourceException("Client with phone number '" + client.getPhone() + "' already exists.");
        }
        return clientRepository.save(client);
    }

    @Override
    public Client updateClientPhone(Long id, String newPhone) {
        Optional<Client> clientOpt = clientRepository.findById(id);
        if (clientOpt.isPresent()) {
            Client client = clientOpt.get();
            client.setPhone(newPhone);
            return clientRepository.save(client);
        }
        throw new NoSuchElementException("Client with ID " + id + " not found.");
    }

    @Override
    public void deleteClient(Long id) {
        Optional<Client> client = clientRepository.findById(id);
        if (client.isPresent()) {
            clientRepository.deleteById(id);
        }
        else{
            throw new NoSuchElementException("Client with ID " + id + " not found.");
        }
    }

    @Override
    public Review addReviewToClient(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByClientId(Long clientId) {
        Optional<Client> clientOpt = clientRepository.findById(clientId);
        if (!clientOpt.isPresent()) {
            throw new NoSuchElementException("Client with ID " + clientId + " not found.");
        }
        return reviewRepository.findByClient_Id(clientId);
    }

    @Override
    public Optional<Review> getReviewById(Long id) {
        return reviewRepository.findById(id);
    }

    @Override
    public void deleteReview(Long id) {
        Optional<Review> review = reviewRepository.findById(id);
        if (review.isPresent()) {
            reviewRepository.deleteById(id);
        }
        else {
            throw new NoSuchElementException("Review with ID " + id + " not found.");
        }
    }
}