package au.com.demo.clientservice.service;

import au.com.demo.clientservice.client.v1.model.Client;

public interface ClientService {
    Client getClient(String id);

    Client addClient(Client client);

    String deleteClient(String id);

    Client updateClient(Client client);
}
