package aapostol.libraryManagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import aapostol.libraryManagement.json.Client;

import java.util.List;
import java.util.Optional;

public interface JPAClientRepository extends JpaRepository<Client, Long>{
    public List<Client> findByNameContainingIgnoreCase(String name);
    public Optional<Client> findByPhone(String phone);
}
