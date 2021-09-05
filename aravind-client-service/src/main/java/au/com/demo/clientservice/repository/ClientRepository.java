package au.com.demo.clientservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.demo.clientservice.entity.ClientEntity;

public interface ClientRepository extends JpaRepository<ClientEntity,Long> {
}
