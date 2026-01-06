package aapostol.libraryManagement.mapper;

import org.springframework.stereotype.Component;

import aapostol.libraryManagement.dto.ClientRequest;
import aapostol.libraryManagement.json.Client;

@Component
public class ClientMapper {

    public Client toEntity(ClientRequest clientRequest) {
        Client client = new Client();
        client.setName(clientRequest.getName());
        client.setPhone(clientRequest.getPhone());
        client.setRegistrationDate(new java.util.Date());
        return client;
    }
}
