package aapostol.libraryManagement.service;

import org.springframework.stereotype.Service;

import aapostol.libraryManagement.json.Client;

import java.util.List;

@Service
public interface ClientService {

    public List<Client> getAllClients();
    public List<Client> getClientsByName(String name);
    public Client getClientById(Long id);
    public Client getClientByPhone(String phone);
    public Client addClient(Client client);
    public Client updateClientPhone(Long id, String newPhone);
    public void deleteClient(Long id);

}
