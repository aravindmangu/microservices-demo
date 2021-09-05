package au.com.demo.clientservice.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import au.com.demo.clientservice.client.v1.model.Client;
import au.com.demo.clientservice.entity.ClientEntity;
import au.com.demo.clientservice.entity.mapper.ClientMapper;
import au.com.demo.clientservice.exceptions.RecordNotFoundException;
import au.com.demo.clientservice.repository.ClientRepository;
import au.com.demo.clientservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repo;

    @Override
    public Client getClient(String id) throws RecordNotFoundException {
        log.info("Trying to get client with ID "+ id);
        Optional<ClientEntity> optionalRecord = repo.findById(Long.parseLong(id));

        if(!optionalRecord.isPresent()) {
            log.error("Client with ID " + id + " does not exist.");
            throw new RecordNotFoundException("Client with ID " + id + " does not exist.");
        }

        return ClientMapper.toModel(optionalRecord.get());
    }

    @Override
    public Client addClient(Client client) {
        ClientEntity ce = repo.save(ClientMapper.toEntity(client));
        log.info("Created client with ID "+ ce.getId());
        return ClientMapper.toModel(ce);
    }

    @Override
    public String deleteClient(String id) throws RecordNotFoundException {
        Optional<ClientEntity> optionalRecord = repo.findById(Long.parseLong(id));

        if(!optionalRecord.isPresent()) {
            log.error("Client with ID " + id + " does not exist.");
            throw new RecordNotFoundException("Client with ID " + id + " does not exist.");
        }

        repo.deleteById(Long.parseLong(id));
        log.info("Deleted client with ID "+ id);

        return "Deleted client with ID "+ id;
    }

    @Override
    public Client updateClient(Client client) {
        Optional<ClientEntity> optionalRecord = repo.findById(Long.parseLong(client.getId()));

        if(!optionalRecord.isPresent()) {
            log.error("Client with ID " + client.getId() + " does not exist.");
            throw new RecordNotFoundException("Client with ID " + client.getId() + " does not exist.");
        }

        ClientEntity ce = optionalRecord.get();
        ce.setName(client.getName());
        ce.setEmail(client.getEmail());

        ClientEntity ce1 = repo.save(ce);
        log.info("Updated client with ID "+ ce1.getId());
        return ClientMapper.toModel(ce1);
    }

}
